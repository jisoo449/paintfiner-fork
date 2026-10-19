#!/usr/bin/env bash
set -uo pipefail

while read -r dir repo <&3; do
  [[ -z "${dir:-}" ]] && continue
  out="issues/$dir"; mkdir -p "$out"
  echo ">>> $repo → $out"

  gh issue list -R "$repo" --state all --limit 10000 --json number -q '.[].number' < /dev/null |
  while read -r n; do
    gh issue view "$n" -R "$repo" \
      --json number,title,state,author,createdAt,closedAt,labels,url,body,comments \
      --jq '"# #\(.number) \(.title)\n\n" +
            "- 상태: \(.state)\n- 작성자: \(.author.login)\n" +
            "- 생성: \(.createdAt)\n- 종료: \(.closedAt // "-")\n" +
            "- 라벨: \([.labels[].name] | join(", "))\n- 원본: \(.url)\n\n" +
            "## 본문\n\n\(.body)\n\n## 댓글\n\n" +
            ([.comments[] | "### \(.author.login) (\(.createdAt))\n\n\(.body)\n"] | join("\n"))' \
      < /dev/null > "$out/$(printf %04d "$n").md" \
      && echo "  #$n" || echo "  !!! #$n 실패"
  done
done 3<<'EOF'
be        paint-finder/backend
fe        paint-finder/webui
crawler   paint-finder/Crawler
EOF