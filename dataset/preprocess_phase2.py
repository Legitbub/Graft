#!/usr/bin/env python3
"""Phase 2 preprocessing for final Neo4j import-ready CSVs.

Phase 2A:
- Refine schema into users_final.csv and follows_final.csv.

Phase 2B:
- Validate import quality constraints.
"""

from __future__ import annotations

import argparse
import csv
from collections import Counter
from pathlib import Path


def read_csv_rows(path: Path) -> list[dict]:
    with path.open("r", encoding="utf-8", newline="") as f:
        return list(csv.DictReader(f))


def write_csv(path: Path, rows: list[dict], fieldnames: list[str]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)


def synthetic_password(user_id: str) -> str:
    """Generate deterministic synthetic password for demo imports only."""
    return f"pw_{user_id}_demo"


def synthetic_bio(name: str, country: str, activity_score: str) -> str:
    """Generate a simple deterministic synthetic bio."""
    return f"{name} from {country}. Activity score: {activity_score}."


def refine_users(users_rows: list[dict]) -> list[dict]:
    """Map users_synthetic schema into users_final schema."""
    final_rows: list[dict] = []

    for row in users_rows:
        user_id = (row.get("user_id") or "").strip()
        username = (row.get("username") or "").strip()
        name = (row.get("full_name") or row.get("name") or "").strip()
        email = (row.get("email") or "").strip()
        country = (row.get("country") or "").strip()
        age = (row.get("age") or "").strip()
        signup_date = (row.get("signup_date") or "").strip()
        activity_score = (row.get("activity_score") or "").strip()

        final_rows.append(
            {
                "user_id": user_id,
                "username": username,
                "name": name,
                "email": email,
                "password": synthetic_password(user_id),
                "bio": synthetic_bio(name, country, activity_score),
                "country": country,
                "age": age,
                "signup_date": signup_date,
                "activity_score": activity_score,
            }
        )

    return final_rows


def refine_follows(follows_rows: list[dict]) -> list[dict]:
    """Map follows_directed schema into follows_final schema."""
    refined: list[dict] = []

    for row in follows_rows:
        follower_id = (row.get("follower_id") or "").strip()
        followee_id = (row.get("followee_id") or "").strip()
        refined.append({"follower_id": follower_id, "followee_id": followee_id})

    return refined


def count_null_required(rows: list[dict], required_fields: list[str]) -> dict[str, int]:
    """Count blank required fields by column."""
    null_counts: dict[str, int] = {col: 0 for col in required_fields}

    for row in rows:
        for col in required_fields:
            value = row.get(col)
            if value is None or str(value).strip() == "":
                null_counts[col] += 1

    return null_counts


def validate_users(users_final: list[dict]) -> dict:
    required = [
        "user_id",
        "username",
        "name",
        "email",
        "password",
        "bio",
        "country",
        "age",
        "signup_date",
        "activity_score",
    ]

    user_ids = [r["user_id"] for r in users_final]
    usernames = [r["username"] for r in users_final]
    emails = [r["email"] for r in users_final]

    user_id_dupes = sum(c - 1 for c in Counter(user_ids).values() if c > 1)
    username_dupes = sum(c - 1 for c in Counter(usernames).values() if c > 1)
    email_dupes = sum(c - 1 for c in Counter(emails).values() if c > 1)
    null_counts = count_null_required(users_final, required)

    return {
        "required_fields": required,
        "user_id_duplicate_count": user_id_dupes,
        "username_duplicate_count": username_dupes,
        "email_duplicate_count": email_dupes,
        "null_required_counts": null_counts,
        "is_valid": (
            user_id_dupes == 0
            and username_dupes == 0
            and email_dupes == 0
            and all(v == 0 for v in null_counts.values())
        ),
    }


def validate_follows(follows_final: list[dict], users_final: list[dict]) -> dict:
    user_id_set = {r["user_id"] for r in users_final}

    missing_follower = 0
    missing_followee = 0
    self_follows = 0

    pairs: list[tuple[str, str]] = []

    for row in follows_final:
        follower_id = row["follower_id"]
        followee_id = row["followee_id"]

        if follower_id not in user_id_set:
            missing_follower += 1
        if followee_id not in user_id_set:
            missing_followee += 1
        if follower_id == followee_id:
            self_follows += 1

        pairs.append((follower_id, followee_id))

    duplicate_pairs = sum(c - 1 for c in Counter(pairs).values() if c > 1)

    return {
        "missing_follower_count": missing_follower,
        "missing_followee_count": missing_followee,
        "self_follow_count": self_follows,
        "duplicate_pair_count": duplicate_pairs,
        "is_valid": (
            missing_follower == 0
            and missing_followee == 0
            and self_follows == 0
            and duplicate_pairs == 0
        ),
    }


def flatten_validation_rows(users_report: dict, follows_report: dict) -> list[dict]:
    rows: list[dict] = [
        {"scope": "users", "check": "user_id_unique", "value": users_report["user_id_duplicate_count"] == 0},
        {
            "scope": "users",
            "check": "username_unique",
            "value": users_report["username_duplicate_count"] == 0,
        },
        {"scope": "users", "check": "email_unique", "value": users_report["email_duplicate_count"] == 0},
        {
            "scope": "follows",
            "check": "follower_exists_in_users",
            "value": follows_report["missing_follower_count"] == 0,
        },
        {
            "scope": "follows",
            "check": "followee_exists_in_users",
            "value": follows_report["missing_followee_count"] == 0,
        },
        {
            "scope": "follows",
            "check": "no_self_follows",
            "value": follows_report["self_follow_count"] == 0,
        },
        {
            "scope": "follows",
            "check": "no_duplicate_pairs",
            "value": follows_report["duplicate_pair_count"] == 0,
        },
    ]

    for col, null_count in users_report["null_required_counts"].items():
        rows.append(
            {
                "scope": "users",
                "check": f"no_nulls_required:{col}",
                "value": null_count == 0,
            }
        )

    return rows


def write_validation_report(path: Path, users_report: dict, follows_report: dict) -> None:
    rows = flatten_validation_rows(users_report, follows_report)
    write_csv(path, rows, fieldnames=["scope", "check", "value"])


def main() -> None:
    parser = argparse.ArgumentParser(description="Phase 2A/2B refine and validate pipeline")
    parser.add_argument(
        "--in-dir",
        type=Path,
        default=Path("preprocessed"),
        help="Directory containing Phase 1 CSV outputs (default: preprocessed)",
    )
    parser.add_argument(
        "--out-dir",
        type=Path,
        default=Path("preprocessed/final"),
        help="Directory for final schema CSVs and validation report (default: preprocessed/final)",
    )

    args = parser.parse_args()

    users_in = args.in_dir / "users_synthetic.csv"
    follows_in = args.in_dir / "follows_directed.csv"

    if not users_in.exists():
        raise SystemExit(f"Missing input file: {users_in}")
    if not follows_in.exists():
        raise SystemExit(f"Missing input file: {follows_in}")

    users_rows = read_csv_rows(users_in)
    follows_rows = read_csv_rows(follows_in)

    users_final = refine_users(users_rows)
    follows_final = refine_follows(follows_rows)

    users_out = args.out_dir / "users_final.csv"
    follows_out = args.out_dir / "follows_final.csv"
    validation_out = args.out_dir / "validation_report.csv"

    write_csv(
        users_out,
        users_final,
        fieldnames=[
            "user_id",
            "username",
            "name",
            "email",
            "password",
            "bio",
            "country",
            "age",
            "signup_date",
            "activity_score",
        ],
    )
    write_csv(follows_out, follows_final, fieldnames=["follower_id", "followee_id"])

    users_report = validate_users(users_final)
    follows_report = validate_follows(follows_final, users_final)
    write_validation_report(validation_out, users_report, follows_report)

    print("Phase 2A refinement complete.")
    print(f"- {users_out}")
    print(f"- {follows_out}")
    print("Phase 2B validation complete.")
    print(f"- {validation_out}")
    print("Validation summary:")
    print(f"  Users valid: {users_report['is_valid']}")
    print(f"  Follows valid: {follows_report['is_valid']}")
    print(f"  user_id duplicates: {users_report['user_id_duplicate_count']}")
    print(f"  username duplicates: {users_report['username_duplicate_count']}")
    print(f"  email duplicates: {users_report['email_duplicate_count']}")
    print(f"  missing follower refs: {follows_report['missing_follower_count']}")
    print(f"  missing followee refs: {follows_report['missing_followee_count']}")
    print(f"  self-follows: {follows_report['self_follow_count']}")
    print(f"  duplicate follows pairs: {follows_report['duplicate_pair_count']}")


if __name__ == "__main__":
    main()
