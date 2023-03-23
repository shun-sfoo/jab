# DOC

## database

### create database

```sql
CREATE
DATABASE IF NOT EXISTS RUNOOB DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
```

### dump sql

```bash
docker exec -i mysql_container mysql -uroot -psecret database < database_backup.sql
```