# Redis-MutexLock
Redis互斥锁，避免高并发时，缓存击穿问题

- 使用Redis的setnx命令实现锁
- Redis连接池使用单例模式创建
