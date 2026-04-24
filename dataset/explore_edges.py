#!/usr/bin/env python3
"""Explore Facebook ego-network .edges files.

Each .edges file is expected to contain one undirected edge per line as:
    <node_id_a> <node_id_b>
"""

from __future__ import annotations

import argparse
import csv
from collections import Counter
from pathlib import Path
from statistics import mean
from typing import Iterable


def iter_edge_files(data_dir: Path) -> list[Path]:
    """Return sorted list of .edges files in the data directory."""
    return sorted(data_dir.glob("*.edges"))


def parse_edges(file_path: Path) -> tuple[list[tuple[int, int]], int]:
    """Parse edges from a file and return (edges, malformed_line_count)."""
    edges: list[tuple[int, int]] = []
    malformed = 0

    with file_path.open("r", encoding="utf-8") as f:
        for raw_line in f:
            line = raw_line.strip()
            if not line:
                continue

            parts = line.split()
            if len(parts) != 2:
                malformed += 1
                continue

            try:
                a, b = int(parts[0]), int(parts[1])
            except ValueError:
                malformed += 1
                continue

            edges.append((a, b))

    return edges, malformed


def canonical_edge(a: int, b: int) -> tuple[int, int]:
    """Return a canonical undirected representation of an edge."""
    return (a, b) if a <= b else (b, a)


def degree_counter(edges: Iterable[tuple[int, int]]) -> Counter[int]:
    """Compute node degree counts for an undirected edge list."""
    degree: Counter[int] = Counter()
    for a, b in edges:
        degree[a] += 1
        degree[b] += 1
    return degree


def degree_distribution(degree: Counter[int]) -> Counter[int]:
    """Map degree value -> number of nodes with that degree."""
    distribution: Counter[int] = Counter()
    for deg in degree.values():
        distribution[deg] += 1
    return distribution


def summarize_file(file_path: Path, top_k: int) -> dict:
    edges, malformed = parse_edges(file_path)
    canonical_edges = [canonical_edge(a, b) for a, b in edges]
    unique_edges = set(canonical_edges)
    self_loops = sum(1 for a, b in edges if a == b)
    duplicate_edges_within_file = len(canonical_edges) - len(unique_edges)

    # Per-file graph metrics are based on unique undirected edges.
    degree = degree_counter(unique_edges)
    nodes = set(degree.keys())

    num_edges = len(edges)
    num_unique_edges = len(unique_edges)
    num_nodes = len(nodes)
    avg_degree = mean(degree.values()) if degree else 0.0
    max_degree = max(degree.values()) if degree else 0

    return {
        "file": file_path.name,
        "num_edges": num_edges,
        "num_unique_edges": num_unique_edges,
        "duplicate_edges_within_file": duplicate_edges_within_file,
        "self_loops": self_loops,
        "num_nodes": num_nodes,
        "malformed": malformed,
        "avg_degree": avg_degree,
        "max_degree": max_degree,
        "degree_distribution": degree_distribution(degree),
        "degree": degree,
        "unique_edges": unique_edges,
        "top_nodes": degree.most_common(top_k),
    }


def format_degree_distribution(distribution: Counter[int], limit: int) -> list[str]:
    """Return formatted degree distribution lines (degree -> node count)."""
    items = sorted(distribution.items(), key=lambda x: x[0])
    if not items:
        return ["(none)"]

    if limit > 0 and len(items) > limit:
        shown = items[:limit]
        lines = [f"degree {deg}: {count} node(s)" for deg, count in shown]
        lines.append(f"... ({len(items) - limit} more degree bucket(s))")
        return lines

    return [f"degree {deg}: {count} node(s)" for deg, count in items]


def print_file_summary(summary: dict, degree_dist_limit: int) -> None:
    print(f"\nFile: {summary['file']}")
    print(f"  Edges (raw): {summary['num_edges']}")
    print(f"  Unique undirected edges: {summary['num_unique_edges']}")
    print(f"  Duplicate edges within file: {summary['duplicate_edges_within_file']}")
    print(f"  Self-loops: {summary['self_loops']}")
    print(f"  Nodes: {summary['num_nodes']}")
    print(f"  Malformed lines skipped: {summary['malformed']}")
    print(f"  Avg degree: {summary['avg_degree']:.2f}")
    print(f"  Max degree: {summary['max_degree']}")
    print("  Degree distribution:")
    for line in format_degree_distribution(summary["degree_distribution"], degree_dist_limit):
        print(f"    {line}")
    print("  Top nodes by degree:")
    if not summary["top_nodes"]:
        print("    (none)")
    else:
        for node, deg in summary["top_nodes"]:
            print(f"    Node {node}: degree {deg}")


def print_overall(summaries: list[dict], top_k: int, degree_dist_limit: int) -> None:
    total_edges = sum(s["num_edges"] for s in summaries)
    total_unique_edges_per_file_sum = sum(s["num_unique_edges"] for s in summaries)
    total_nodes_sum = sum(s["num_nodes"] for s in summaries)
    total_malformed = sum(s["malformed"] for s in summaries)
    total_self_loops = sum(s["self_loops"] for s in summaries)
    total_within_file_duplicates = sum(s["duplicate_edges_within_file"] for s in summaries)

    edge_to_files: dict[tuple[int, int], set[str]] = {}
    for summary in summaries:
        file_name = summary["file"]
        for edge in summary["unique_edges"]:
            edge_to_files.setdefault(edge, set()).add(file_name)

    global_unique_edges = len(edge_to_files)
    global_duplicates_removed = total_edges - global_unique_edges

    cross_file_duplicate_edges = sum(1 for files in edge_to_files.values() if len(files) > 1)
    cross_file_duplicate_instances = sum(len(files) - 1 for files in edge_to_files.values())

    # Aggregate across all parsed file degrees for an accurate global ranking.
    combined_degrees = degree_counter(edge_to_files.keys())
    combined_distribution = degree_distribution(combined_degrees)

    unique_nodes_global = len(combined_degrees)

    print("\n=== Overall Summary ===")
    print(f"Edge files processed: {len(summaries)}")
    print(f"Total edges (all files, raw): {total_edges}")
    print(f"Sum of per-file unique edges: {total_unique_edges_per_file_sum}")
    print(f"Global unique undirected edges: {global_unique_edges}")
    print(f"Global duplicates removed (raw - global unique): {global_duplicates_removed}")
    print(f"Duplicates within files (sum): {total_within_file_duplicates}")
    print(f"Duplicate edge patterns across files: {cross_file_duplicate_edges}")
    print(f"Cross-file duplicate instances (extra file memberships): {cross_file_duplicate_instances}")
    print(f"Total self-loops (raw): {total_self_loops}")
    print(f"Sum of per-file node counts: {total_nodes_sum}")
    print(f"Unique nodes across all files: {unique_nodes_global}")
    print(f"Total malformed lines skipped: {total_malformed}")
    print("Global degree distribution (deduplicated):")
    for line in format_degree_distribution(combined_distribution, degree_dist_limit):
        print(f"  {line}")
    print(f"Top {top_k} nodes by global degree:")
    if not combined_degrees:
        print("  (none)")
    else:
        for node, deg in combined_degrees.most_common(top_k):
            print(f"  Node {node}: degree score {deg}")


def build_overall_stats(summaries: list[dict]) -> dict:
    """Compute full overall metrics used by console output and CSV export."""
    total_edges = sum(s["num_edges"] for s in summaries)
    total_unique_edges_per_file_sum = sum(s["num_unique_edges"] for s in summaries)
    total_nodes_sum = sum(s["num_nodes"] for s in summaries)
    total_malformed = sum(s["malformed"] for s in summaries)
    total_self_loops = sum(s["self_loops"] for s in summaries)
    total_within_file_duplicates = sum(s["duplicate_edges_within_file"] for s in summaries)

    edge_to_files: dict[tuple[int, int], set[str]] = {}
    for summary in summaries:
        file_name = summary["file"]
        for edge in summary["unique_edges"]:
            edge_to_files.setdefault(edge, set()).add(file_name)

    global_unique_edges = len(edge_to_files)
    global_duplicates_removed = total_edges - global_unique_edges

    cross_file_duplicate_edges = sum(1 for files in edge_to_files.values() if len(files) > 1)
    cross_file_duplicate_instances = sum(len(files) - 1 for files in edge_to_files.values())

    combined_degrees = degree_counter(edge_to_files.keys())
    combined_distribution = degree_distribution(combined_degrees)
    unique_nodes_global = len(combined_degrees)

    return {
        "total_edges": total_edges,
        "total_unique_edges_per_file_sum": total_unique_edges_per_file_sum,
        "global_unique_edges": global_unique_edges,
        "global_duplicates_removed": global_duplicates_removed,
        "total_within_file_duplicates": total_within_file_duplicates,
        "cross_file_duplicate_edges": cross_file_duplicate_edges,
        "cross_file_duplicate_instances": cross_file_duplicate_instances,
        "total_self_loops": total_self_loops,
        "total_nodes_sum": total_nodes_sum,
        "unique_nodes_global": unique_nodes_global,
        "total_malformed": total_malformed,
        "combined_degrees": combined_degrees,
        "combined_distribution": combined_distribution,
    }


def write_csv_report(csv_path: Path, summaries: list[dict], overall: dict, top_k: int) -> None:
    """Write full edge checks and stats into one long-form CSV file."""
    csv_path.parent.mkdir(parents=True, exist_ok=True)

    fieldnames = [
        "row_type",
        "scope",
        "name",
        "metric",
        "value",
        "degree",
        "node_count",
        "rank",
        "node",
        "node_degree",
    ]

    with csv_path.open("w", newline="", encoding="utf-8") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()

        per_file_metrics = [
            "num_edges",
            "num_unique_edges",
            "duplicate_edges_within_file",
            "self_loops",
            "num_nodes",
            "malformed",
            "avg_degree",
            "max_degree",
        ]
        for summary in summaries:
            for metric in per_file_metrics:
                writer.writerow(
                    {
                        "row_type": "metric",
                        "scope": "file",
                        "name": summary["file"],
                        "metric": metric,
                        "value": summary[metric],
                    }
                )

            for deg, count in sorted(summary["degree_distribution"].items()):
                writer.writerow(
                    {
                        "row_type": "degree_distribution",
                        "scope": "file",
                        "name": summary["file"],
                        "degree": deg,
                        "node_count": count,
                    }
                )

            for rank, (node, node_degree) in enumerate(summary["top_nodes"], start=1):
                writer.writerow(
                    {
                        "row_type": "top_node",
                        "scope": "file",
                        "name": summary["file"],
                        "rank": rank,
                        "node": node,
                        "node_degree": node_degree,
                    }
                )

        overall_metric_map = {
            "edge_files_processed": len(summaries),
            "total_edges_raw": overall["total_edges"],
            "sum_per_file_unique_edges": overall["total_unique_edges_per_file_sum"],
            "global_unique_edges": overall["global_unique_edges"],
            "global_duplicates_removed": overall["global_duplicates_removed"],
            "duplicates_within_files_sum": overall["total_within_file_duplicates"],
            "duplicate_edge_patterns_across_files": overall["cross_file_duplicate_edges"],
            "cross_file_duplicate_instances": overall["cross_file_duplicate_instances"],
            "total_self_loops_raw": overall["total_self_loops"],
            "sum_per_file_node_counts": overall["total_nodes_sum"],
            "unique_nodes_global": overall["unique_nodes_global"],
            "total_malformed_lines": overall["total_malformed"],
        }

        for metric, value in overall_metric_map.items():
            writer.writerow(
                {
                    "row_type": "metric",
                    "scope": "overall",
                    "name": "all_files",
                    "metric": metric,
                    "value": value,
                }
            )

        for deg, count in sorted(overall["combined_distribution"].items()):
            writer.writerow(
                {
                    "row_type": "degree_distribution",
                    "scope": "overall",
                    "name": "all_files",
                    "degree": deg,
                    "node_count": count,
                }
            )

        for rank, (node, node_degree) in enumerate(
            overall["combined_degrees"].most_common(top_k), start=1
        ):
            writer.writerow(
                {
                    "row_type": "top_node",
                    "scope": "overall",
                    "name": "all_files",
                    "rank": rank,
                    "node": node,
                    "node_degree": node_degree,
                }
            )


def main() -> None:
    parser = argparse.ArgumentParser(
        description="Explore .edges files in the Facebook ego-network dataset."
    )
    parser.add_argument(
        "--data-dir",
        type=Path,
        default=Path("facebook"),
        help="Directory containing .edges files (default: facebook)",
    )
    parser.add_argument(
        "--top-k",
        type=int,
        default=5,
        help="Number of top-degree nodes to display per file (default: 5)",
    )
    parser.add_argument(
        "--degree-dist-limit",
        type=int,
        default=15,
        help=(
            "Max number of degree buckets to print for each distribution; "
            "use 0 to print all"
        ),
    )
    parser.add_argument(
        "--csv-out",
        type=Path,
        default=None,
        help="Optional output CSV path for full stats/checks report",
    )

    args = parser.parse_args()

    edge_files = iter_edge_files(args.data_dir)
    if not edge_files:
        raise SystemExit(f"No .edges files found in: {args.data_dir}")

    print(f"Found {len(edge_files)} .edges files in {args.data_dir}")

    summaries: list[dict] = []
    for edge_file in edge_files:
        summary = summarize_file(edge_file, args.top_k)
        summaries.append(summary)
        print_file_summary(summary, args.degree_dist_limit)

    overall = build_overall_stats(summaries)

    print_overall(summaries, args.top_k, args.degree_dist_limit)

    if args.csv_out is not None:
        write_csv_report(args.csv_out, summaries, overall, args.top_k)
        print(f"\nCSV report written to: {args.csv_out}")


if __name__ == "__main__":
    main()
