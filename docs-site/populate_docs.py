"""
Populate the Astro Starlight docs-site from the canonical docs/ directory.

Walks docs/ recursively and mirrors the structure into
docs-site/src/content/docs/, adding clean YAML frontmatter to every .md file
and copying non-.md assets (e.g. SVGs) into both the content tree and
docs-site/public/images/ for web access.
"""

import re
import shutil
from pathlib import Path

# ---------------------------------------------------------------------------
# Paths – all relative to *this* script so the repo can live anywhere
# ---------------------------------------------------------------------------
SCRIPT_DIR = Path(__file__).resolve().parent          # docs-site/
PROJECT_ROOT = SCRIPT_DIR.parent                       # repo root
DOCS_SRC = PROJECT_ROOT / "docs"
CONTENT_DEST = SCRIPT_DIR / "src" / "content" / "docs"
PUBLIC_DEST = SCRIPT_DIR / "public"

# Pre-existing landing page we never overwrite
SKIP_FILENAMES = {"index.md"}

# Regex that matches a YAML frontmatter block at the very start of a file
FRONTMATTER_RE = re.compile(r"\A---\s*\n.*?\n---\s*\n?", re.DOTALL)

# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

def _slug_from_path(path: Path) -> str:
    """Derive a human-readable title from a filename stem."""
    return path.stem.replace("-", " ").replace("_", " ").title()


def _process_markdown(src: Path, dst: Path) -> None:
    """Read a .md file, strip old frontmatter, extract H1, write with clean frontmatter."""
    content = src.read_text(encoding="utf-8")

    # Strip existing YAML frontmatter
    content = FRONTMATTER_RE.sub("", content).lstrip()

    # Extract H1 title (first line starting with '# ')
    lines = content.split("\n")
    title = _slug_from_path(src)  # fallback
    if lines and lines[0].startswith("# "):
        title = lines[0].lstrip("# ").strip()
        lines = lines[1:]  # remove H1 – Starlight generates it from frontmatter
        content = "\n".join(lines).lstrip()

    dst.parent.mkdir(parents=True, exist_ok=True)
    dst.write_text(
        f'---\ntitle: "{title}"\n---\n\n{content}',
        encoding="utf-8",
    )


def _copy_asset(src: Path, rel: Path) -> None:
    """Copy a non-.md file into the content tree and into public/images/."""
    # Mirror into content docs tree
    content_dst = CONTENT_DEST / rel
    content_dst.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(src, content_dst)

    # Also copy to public/images/ for direct web access
    public_dst = PUBLIC_DEST / "images" / rel
    public_dst.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(src, public_dst)


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main() -> None:
    md_count = 0
    asset_count = 0

    for src in sorted(DOCS_SRC.rglob("*")):
        if not src.is_file():
            continue

        rel = src.relative_to(DOCS_SRC)

        if rel.name in SKIP_FILENAMES:
            continue

        if src.suffix == ".md":
            dst = CONTENT_DEST / rel
            _process_markdown(src, dst)
            md_count += 1
        else:
            _copy_asset(src, rel)
            asset_count += 1

    print(f"Synced {md_count} markdown files and {asset_count} assets.")


if __name__ == "__main__":
    main()
