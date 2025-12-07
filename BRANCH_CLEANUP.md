# Branch Cleanup Documentation

## Overview
This document outlines the branch cleanup process for the PermisConnect repository.

## Current Branch Status

### Branches to Keep
- `main` - Main production branch
- `develop` - Development branch

### Branches to Remove
The following branches are old and should be removed:
1. `backend`
2. `backtofront`
3. `fixbugs`
4. `frontend`
5. `new`
6. `new-backend`
7. `new-frontend`
8. `nouveau`

## Manual Cleanup Instructions

To delete these branches remotely, you can use the GitHub UI or the following git commands:

### Using Git Commands

```bash
# Delete remote branches
git push origin --delete backend
git push origin --delete backtofront
git push origin --delete fixbugs
git push origin --delete frontend
git push origin --delete new
git push origin --delete new-backend
git push origin --delete new-frontend
git push origin --delete nouveau
```

### Using GitHub UI

1. Navigate to the repository on GitHub
2. Go to the "Branches" page (Code → Branches)
3. For each branch listed above, click the trash icon to delete it
4. Confirm the deletion

## Automated Cleanup Script

A cleanup script (`cleanup-branches.sh`) has been provided in the repository root for automated deletion.

## Post-Cleanup Verification

After cleanup, verify that only `main` and `develop` branches remain:

```bash
git fetch --prune
git branch -r
```

Expected output:
```
origin/develop
origin/main
```
