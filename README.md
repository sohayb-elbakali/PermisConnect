# PermisConnect

## Repository Structure

This repository follows a simplified branch strategy:

- **main** - Production-ready code
- **develop** - Development branch for ongoing work

## Branch Management

### Cleanup of Old Branches

As part of repository maintenance, old feature branches have been removed. Only `main` and `develop` branches are maintained.

For details on the cleanup process, see [BRANCH_CLEANUP.md](./BRANCH_CLEANUP.md).

### Branch Cleanup

To clean up old branches, you have several options:

1. **GitHub Actions** (Recommended): Use the "Branch Cleanup" workflow in the Actions tab
2. **Shell Script**: Run `./cleanup-branches.sh` locally
3. **Manual**: Delete branches via GitHub UI or git commands

See [BRANCH_CLEANUP.md](./BRANCH_CLEANUP.md) for detailed instructions.

All methods will remove old branches and keep only `main` and `develop`.

## Development

The repository contains:
- `backend/` - Backend application code
- `frontend/` - Frontend application code

For frontend-specific documentation, see [frontend/README.md](./frontend/README.md).

## Contributing

When contributing to this repository:
1. Create feature branches from `develop`
2. Submit pull requests to `develop`
3. Only maintainers merge to `main`
