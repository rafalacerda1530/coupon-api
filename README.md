# Coupon API
API REST para gerenciamento de cupons de desconto.
## Stack
- Java 17
- Spring Boot 3.2.2
- H2 Database (in-memory)
- Maven
- Docker
## Requisitos
- JDK 17+
- Maven 3.8+
- Docker (opcional)
## Como rodar
### Local
\\\Bash
mvn clean install
mvn spring-boot:run
\\\
### Docker
\\\Bash
docker-compose up --build
\\\
A aplicação sobe na porta **8080**.
## Endpoints
### Criar cupom
\\\
POST /api/v1/coupons
Content-Type: application/json
{
  "code": "SAVE10",
  "description": "Desconto de 10 reais",
  "discountValue": 10.0,
  "expirationDate": "2026-12-31T23:59:59",
  "published": true
}
\\\
### Buscar cupom
\\\
GET /api/v1/coupons/{id}
\\\
### Deletar cupom
\\\
DELETE /api/v1/coupons/{id}
\\\
## Regras de negócio
- Código do cupom: 6 caracteres alfanuméricos (caracteres especiais são removidos)
- Valor mínimo de desconto: 0.5
- Data de expiração não pode estar no passado
- Descrição: máximo 255 caracteres
- Deleção é lógica (soft delete)
## Documentação
Swagger disponível em: \http://localhost:8080/swagger-ui.html\
## Banco de dados
H2 Console: \http://localhost:8080/h2-console\
**Configuração:**
- JDBC URL: \jdbc:h2:mem:coupondb\
- User: \
oot\
- Password: (deixar em branco)
## Testes
\\\ash
mvn test
\\\
Cobertura: 100% das regras de negócio
## Arquitetura
O projeto segue arquitetura hexagonal (ports & adapters):
- **adapters**: Camada de adaptadores (controllers, repositories)
- **application/core**: Lógica de negócio e casos de uso
- **application/port**: Interfaces (contratos)
Regras de negócio estão encapsuladas nos objetos de domínio.
## Notas técnicas
- Soft delete implementado via campo status
- Validações no domínio (fail-fast)
- Exception handling centralizado
- DTOs para separar camadas
