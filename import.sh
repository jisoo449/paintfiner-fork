#!/usr/bin/env bash
set -e
while read -r path repo; do
  [ -z "$path" ] && continue
  branch=$(gh repo view "$repo" --json defaultBranchRef -q .defaultBranchRef.name)
  echo ">>> $repo ($branch) → $path"
  git subtree add --prefix="$path" "https://github.com/$repo.git" "$branch"
done <<'EOF'
paintfiner-be-fork        paint-finder/backend
paintfinder-fe-fork        paint-finder/webui
paintfiner-crawler-fork   paint-finder/Crawler

EOF
