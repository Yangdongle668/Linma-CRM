# Contributing to Foreign Trade CRM

Thank you for your interest in contributing to Foreign Trade CRM! This document provides guidelines and instructions for contributing.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Making Changes](#making-changes)
- [Submitting Changes](#submitting-changes)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)

## Code of Conduct

Please read and follow our [Code of Conduct](CODE_OF_CONDUCT.md).

## Getting Started

1. **Fork** the repository on GitHub
2. **Clone** your fork locally
   ```bash
   git clone https://github.com/YOUR_USERNAME/foreign-trade-crm.git
   cd foreign-trade-crm
   ```
3. **Add upstream remote**
   ```bash
   git remote add upstream https://github.com/ORIGINAL_OWNER/foreign-trade-crm.git
   ```

## Development Setup

### Prerequisites

- JDK 17+
- Node.js 18+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0 (or use Docker)

### Backend Setup

```bash
# Build project
mvn clean install

# Start infrastructure services
docker-compose up -d mysql redis rabbitmq

# Run backend
cd crm-admin
mvn spring-boot:run
```

### Frontend Setup

```bash
cd crm-frontend
npm install
npm run dev
```

## Making Changes

1. **Create a branch** from `develop`
   ```bash
   git checkout develop
   git pull upstream develop
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following our coding standards

3. **Test your changes**
   ```bash
   # Backend tests
   mvn test

   # Frontend tests
   npm run test
   ```

4. **Commit your changes** using conventional commits

## Submitting Changes

1. **Push** to your fork
   ```bash
   git push origin feature/your-feature-name
   ```

2. **Open a Pull Request** to the `develop` branch

3. **Fill out** the PR template completely

4. **Wait for review** from maintainers

## Coding Standards

### Backend (Java)

- Follow [Alibaba Java Coding Guidelines](https://github.com/alibaba/p3c)
- Use Lombok annotations to reduce boilerplate
- Add Chinese comments for all public methods
- Write unit tests for new features
- Maximum method length: 50 lines
- Maximum class length: 500 lines

**Example:**
```java
/**
 * 创建客户
 *
 * @param dto 客户创建DTO
 * @return 客户ID
 */
@Transactional
public Long createCustomer(CustomerCreateDTO dto) {
    // 检查重复
    checkDuplicate(dto);

    // 转换并保存
    Customer customer = customerConverter.toEntity(dto);
    customerMapper.insert(customer);

    return customer.getId();
}
```

### Frontend (Vue + TypeScript)

- Use Composition API with `<script setup>`
- Follow Vue Style Guide
- Use TypeScript for type safety
- Component names: PascalCase
- File names: kebab-case
- Maximum component size: 300 lines

**Example:**
```vue
<script setup lang="ts">
import { ref, computed } from 'vue'
import type { Customer } from '@/types/customer'

interface Props {
  customerId: number
}

const props = defineProps<Props>()
const emit = defineEmits<{
  (e: 'update', customer: Customer): void
}>()

const loading = ref(false)
const customer = ref<Customer | null>(null)

const customerName = computed(() => customer.value?.companyName || '')

async function loadCustomer() {
  loading.value = true
  try {
    customer.value = await getCustomerDetail(props.customerId)
  } finally {
    loading.value = false
  }
}
</script>
```

### Database

- Table names: `snake_case` with prefix (`sys_`, `crm_`, `msg_`)
- Column names: `snake_case`
- Always include audit fields: `created_by`, `created_time`, `updated_by`, `updated_time`
- Use logical deletion: `deleted` field
- Add indexes for frequently queried columns

## Commit Guidelines

We follow [Conventional Commits](https://www.conventionalcommits.org/):

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation only
- `style`: Formatting, missing semicolons, etc.
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Examples:**
```bash
feat(customer): add customer duplicate detection
fix(order): fix order status transition bug
docs(readme): update installation instructions
refactor(analytics): simplify funnel calculation logic
```

## Pull Request Process

### Before Submitting

- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Code is commented where necessary
- [ ] Tests added and passing
- [ ] Documentation updated
- [ ] No merge conflicts

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] Unit tests added/updated
- [ ] Manually tested

## Screenshots (if applicable)

## Checklist
- [ ] My code follows the project's style guidelines
- [ ] I have performed a self-review
- [ ] I have commented my code
- [ ] I have added tests
- [ ] All tests pass
- [ ] Documentation updated
```

### Review Process

1. **Automated Checks**: CI must pass
2. **Code Review**: At least 1 maintainer approval required
3. **Testing**: Verify changes work as expected
4. **Merge**: Squash and merge to `develop`

## Release Process

1. Version bump in `pom.xml`
2. Update CHANGELOG.md
3. Create release tag: `v1.0.0`
4. Build and publish Docker images
5. Deploy to production

## Questions?

- 📧 Email: support@yourcompany.com
- 💬 Discussions: [GitHub Discussions](https://github.com/YOUR_USERNAME/foreign-trade-crm/discussions)
- 🐛 Issues: [GitHub Issues](https://github.com/YOUR_USERNAME/foreign-trade-crm/issues)

---

Thank you for contributing! 🎉
