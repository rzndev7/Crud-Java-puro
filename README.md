# CRUD de Clientes (Java puro)

Um pequeno sistema de cadastro de clientes (CRUD) em Java puro, pronto para rodar no Windows. Não usa frameworks nem gerenciadores de dependência.

## Requisitos
- JDK 8+ instalado e acessível no PATH (javac/java)
- Windows (scripts .bat incluídos)

## Como executar

1. Compile e gere o JAR:

```powershell
# No PowerShell
cd "c:\Users\PC Gamer\Desktop\crud"
./build.bat
```

2. Execute o aplicativo:

```powershell
./run.bat
```

3. Limpeza (opcional):

```powershell
./clean.bat
```

Os dados são salvos em `data/customers.csv` no formato CSV simples.

## Funcionalidades
- Listar clientes
- Cadastrar novo cliente
- Buscar cliente por ID
- Atualizar cliente
- Remover cliente
- Persistência em arquivo CSV (sem libs externas)

## Observações
- CSV simples: campos com vírgula não são recomendados. Aspas duplas são escapadas automaticamente, mas evite vírgulas para manter a simplicidade.
- O arquivo `customers.csv` é criado automaticamente na primeira execução de gravação.

## Estrutura
```
crud/
  ├─ data/
  │   └─ customers.csv
  ├─ src/
  │   └─ com/example/crud/
  │       ├─ Customer.java
  │       ├─ CustomerRepository.java
  │       └─ Main.java
  ├─ build.bat
  ├─ run.bat
  ├─ clean.bat
  └─ README.md
```

