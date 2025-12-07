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

## Cleanup Methods

There are three ways to clean up the old branches:

### Method 1: GitHub Actions Workflow (Recommended)

The easiest and safest method is to use the GitHub Actions workflow:

1. Go to the repository on GitHub
2. Navigate to "Actions" tab
3. Select "Branch Cleanup" workflow from the left sidebar
4. Click "Run workflow"
5. Type "yes" in the confirmation field
6. Click "Run workflow" button

The workflow will automatically delete all old branches.

### Method 2: Using Git Commands

If you have repository write access, run these commands locally:

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

Or use the provided automated script:

```bash
./cleanup-branches.sh
```

### Method 3: Using GitHub UI

1. Navigate to the repository on GitHub
2. Go to the "Branches" page (Code → Branches)
3. For each branch listed above, click the trash icon to delete it
4. Confirm the deletion

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
