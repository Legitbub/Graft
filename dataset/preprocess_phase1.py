#!/usr/bin/env python3
"""Phase 1 preprocessing for Facebook edge files.

Steps:
1) Merge all .edges files
2) Deduplicate globally (undirected)
3) Remove self-loops
4) Extract unique users
5) Generate synthetic user fields
6) Create directed follows
7) Export CSVs
"""

from __future__ import annotations

import argparse
import csv
import datetime as dt
from pathlib import Path
from random import Random


def canonical_edge(a: int, b: int) -> tuple[int, int]:
    """Return canonical undirected edge ordering."""
    return (a, b) if a <= b else (b, a)


def read_and_merge_edges(data_dir: Path) -> tuple[list[dict], int, int]:
    """Read all .edges files and return merged rows, malformed count, file count."""
    edge_files = sorted(data_dir.glob("*.edges"))
    if not edge_files:
        raise SystemExit(f"No .edges files found in: {data_dir}")

    merged_rows: list[dict] = []
    malformed_lines = 0

    for file_path in edge_files:
        with file_path.open("r", encoding="utf-8") as f:
            for line_num, raw in enumerate(f, start=1):
                line = raw.strip()
                if not line:
                    continue

                parts = line.split()
                if len(parts) != 2:
                    malformed_lines += 1
                    continue

                try:
                    u = int(parts[0])
                    v = int(parts[1])
                except ValueError:
                    malformed_lines += 1
                    continue

                merged_rows.append(
                    {
                        "source_file": file_path.name,
                        "line_number": line_num,
                        "src": u,
                        "dst": v,
                    }
                )

    return merged_rows, malformed_lines, len(edge_files)


def deduplicate_and_filter(merged_rows: list[dict]) -> tuple[list[tuple[int, int]], int, int]:
    """Deduplicate edges globally and remove self-loops."""
    deduped: set[tuple[int, int]] = set()
    self_loops = 0

    for row in merged_rows:
        u = int(row["src"])
        v = int(row["dst"])

        if u == v:
            self_loops += 1
            continue

        deduped.add(canonical_edge(u, v))

    duplicate_count = len(merged_rows) - self_loops - len(deduped)
    return sorted(deduped), self_loops, duplicate_count


def extract_users(edges_undirected: list[tuple[int, int]]) -> list[int]:
    """Extract sorted unique user IDs from undirected edges."""
    users: set[int] = set()
    for u, v in edges_undirected:
        users.add(u)
        users.add(v)
    return sorted(users)


def synthetic_user_row(user_id: int, seed: int) -> dict:
    """Generate deterministic synthetic user attributes for one user ID."""
    rng = Random(seed + user_id)

    first_names = [
        "Alex",
        "Sam",
        "Jordan",
        "Taylor",
        "Riley",
        "Casey",
        "Morgan",
        "Avery",
    ]
    last_names = [
        "Patel",
        "Nguyen",
        "Smith",
        "Garcia",
        "Kim",
        "Brown",
        "Lee",
        "Davis",
    ]
    countries = ["US", "IN", "CA", "UK", "AU", "DE", "SG", "BR"]
    domains = ["example.com", "mail.com", "network.io", "social.net"]

    first = first_names[rng.randrange(len(first_names))]
    last = last_names[rng.randrange(len(last_names))]
    username = f"{first.lower()}.{last.lower()}.{user_id}"
    email = f"{username}@{domains[rng.randrange(len(domains))]}"

    base_date = dt.date(2010, 1, 1)
    signup_date = base_date + dt.timedelta(days=rng.randint(0, 15 * 365))

    age = rng.randint(18, 65)
    activity_score = round(rng.uniform(0.2, 1.0), 3)

    return {
        "user_id": user_id,
        "username": username,
        "full_name": f"{first} {last}",
        "email": email,
        "country": countries[rng.randrange(len(countries))],
        "age": age,
        "signup_date": signup_date.isoformat(),
        "activity_score": activity_score,
    }


def build_user_rows(user_ids: list[int], seed: int) -> list[dict]:
    """Generate synthetic users table rows."""
    return [synthetic_user_row(uid, seed) for uid in user_ids]


def build_directed_follows(
    edges_undirected: list[tuple[int, int]], seed: int, one_way: bool
) -> list[tuple[int, int]]:
    """Create directed follows from undirected edges.

    Default behavior is reciprocal follows (u->v and v->u).
    Use one_way=True for one deterministic direction per edge.
    """
    follows: list[tuple[int, int]] = []

    if one_way:
        for u, v in edges_undirected:
            # Deterministic orientation without relying on Python hash randomization.
            orientation = (u * 31 + v * 17 + seed) % 2
            follows.append((u, v) if orientation == 0 else (v, u))
    else:
        for u, v in edges_undirected:
            follows.append((u, v))
            follows.append((v, u))

    return follows


def write_csv(path: Path, rows: list[dict], fieldnames: list[str]) -> None:
    """Write a list of dict rows to CSV."""
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)


def main() -> None:
    parser = argparse.ArgumentParser(description="Phase 1 preprocessing pipeline")
    parser.add_argument(
        "--data-dir",
        type=Path,
        default=Path("facebook"),
        help="Input directory containing .edges files (default: facebook)",
    )
    parser.add_argument(
        "--out-dir",
        type=Path,
        default=Path("preprocessed"),
        help="Output directory for generated CSVs (default: preprocessed)",
    )
    parser.add_argument(
        "--seed",
        type=int,
        default=42,
        help="Seed used for deterministic synthetic fields (default: 42)",
    )
    parser.add_argument(
        "--one-way",
        action="store_true",
        help="Create one directed follow per undirected edge (default is reciprocal)",
    )

    args = parser.parse_args()

    # 1) Merge all edge files.
    merged_rows, malformed_lines, file_count = read_and_merge_edges(args.data_dir)

    # 2) Deduplicate globally and 3) remove self-loops.
    unique_edges, self_loops_removed, duplicate_edges_removed = deduplicate_and_filter(
        merged_rows
    )

    # 4) Extract unique users.
    users = extract_users(unique_edges)

    # 5) Generate synthetic user fields.
    user_rows = build_user_rows(users, seed=args.seed)

    # 6) Create directed follows.
    directed_follows = build_directed_follows(
        unique_edges, seed=args.seed, one_way=args.one_way
    )

    # 7) Export CSVs.
    merged_edges_csv = args.out_dir / "merged_edges_raw.csv"
    unique_edges_csv = args.out_dir / "edges_unique_undirected.csv"
    users_csv = args.out_dir / "users_synthetic.csv"
    follows_csv = args.out_dir / "follows_directed.csv"
    summary_csv = args.out_dir / "preprocessing_summary.csv"

    write_csv(
        merged_edges_csv,
        merged_rows,
        fieldnames=["source_file", "line_number", "src", "dst"],
    )

    write_csv(
        unique_edges_csv,
        [{"user_a": u, "user_b": v} for u, v in unique_edges],
        fieldnames=["user_a", "user_b"],
    )

    write_csv(
        users_csv,
        user_rows,
        fieldnames=[
            "user_id",
            "username",
            "full_name",
            "email",
            "country",
            "age",
            "signup_date",
            "activity_score",
        ],
    )

    write_csv(
        follows_csv,
        [{"follower_id": u, "followee_id": v} for u, v in directed_follows],
        fieldnames=["follower_id", "followee_id"],
    )

    summary_rows = [
        {"metric": "edge_files_read", "value": file_count},
        {"metric": "raw_edges_merged", "value": len(merged_rows)},
        {"metric": "malformed_lines_skipped", "value": malformed_lines},
        {"metric": "self_loops_removed", "value": self_loops_removed},
        {"metric": "duplicate_edges_removed_global", "value": duplicate_edges_removed},
        {"metric": "unique_undirected_edges", "value": len(unique_edges)},
        {"metric": "unique_users", "value": len(users)},
        {"metric": "directed_follows", "value": len(directed_follows)},
        {
            "metric": "follows_mode",
            "value": "one_way" if args.one_way else "reciprocal",
        },
    ]
    write_csv(summary_csv, summary_rows, fieldnames=["metric", "value"])

    print("Phase 1 preprocessing complete.")
    print(f"Output directory: {args.out_dir}")
    print(f"- {merged_edges_csv}")
    print(f"- {unique_edges_csv}")
    print(f"- {users_csv}")
    print(f"- {follows_csv}")
    print(f"- {summary_csv}")


if __name__ == "__main__":
    main()
