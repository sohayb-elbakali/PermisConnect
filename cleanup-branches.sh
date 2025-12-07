#!/bin/bash
# Branch Cleanup Script for PermisConnect
# This script removes old branches, keeping only 'main' and 'develop'

set -e

echo "=========================================="
echo "PermisConnect Branch Cleanup Script"
echo "=========================================="
echo ""

# List of branches to delete
BRANCHES_TO_DELETE=(
    "backend"
    "backtofront"
    "fixbugs"
    "frontend"
    "new"
    "new-backend"
    "new-frontend"
    "nouveau"
)

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "The following branches will be deleted:"
for branch in "${BRANCHES_TO_DELETE[@]}"; do
    echo "  - $branch"
done
echo ""

# Confirmation prompt
read -p "Are you sure you want to delete these branches? (yes/no): " confirmation

if [ "$confirmation" != "yes" ]; then
    echo -e "${YELLOW}Cleanup cancelled.${NC}"
    exit 0
fi

echo ""
echo "Starting branch deletion..."
echo ""

# Delete each branch
for branch in "${BRANCHES_TO_DELETE[@]}"; do
    echo -n "Deleting branch '$branch'... "
    
    if git push origin --delete "$branch" 2>/dev/null; then
        echo -e "${GREEN}✓ Deleted${NC}"
    else
        echo -e "${RED}✗ Failed (may not exist or insufficient permissions)${NC}"
    fi
done

echo ""
echo "=========================================="
echo "Cleanup Complete!"
echo "=========================================="
echo ""
echo "Verifying remaining branches..."
git fetch --prune
echo ""
echo "Remote branches:"
git branch -r | grep -E "origin/(main|develop)" || echo "Warning: Expected branches not found"
echo ""
echo -e "${GREEN}Done! Only 'main' and 'develop' branches should remain.${NC}"
