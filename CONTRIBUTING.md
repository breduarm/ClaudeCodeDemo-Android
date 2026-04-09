# Contributing

## Branch Strategy

This repo follows **GitHub Flow**. All changes go through a pull request — no direct pushes to `main`.

### Branch naming

| Prefix | When to use |
|---|---|
| `feat/<scope>` | New feature |
| `fix/<scope>` | Bug fix |
| `docs/<scope>` | Documentation only |
| `chore/<scope>` | Dependencies, config, tooling |

### Workflow

```bash
# 1. Branch from main
git checkout main && git pull
git checkout -b feat/your-feature

# 2. Make your changes and commit (follow Conventional Commits)
git commit -m "feat(scope): short description"

# 3. Push and open a PR
git push -u origin feat/your-feature
gh pr create
```

### Commit messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <short description>

# Types: feat | fix | docs | chore | refactor | test
# Example:
feat(tasklist): add swipe-to-archive gesture
```

### Merge policy

- PRs are **squash merged** into `main` to keep history linear.
- Feature branches are deleted automatically after merge.
- PRs must reference their issue with `Closes #N`.
