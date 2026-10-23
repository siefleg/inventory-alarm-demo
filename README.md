# 仓库库存预警与智能补货系统 (Curriculum Demo)

一个用于软件工程课程演示的 Spring Boot + Vue 项目，核心亮点是**基于历史出库数据的补货量预测算法**，可作为 AI/机器学习方向入门者的算法实践案例。

## 技术栈

- 后端：Java 8 / Spring Boot 2.2 / MyBatis / MySQL 8.0
- 前端：Vue 2 / Element UI / Axios
- 算法：移动平均法（近 30 天日均出库量预测）+ 安全库存约束

## 功能模块

1. **库存预警**：实时对比当前库存与安全库存阈值，按紧急程度分四级（缺货/高危/中度/正常）
2. **补货建议算法**：`建议补货量 = ceil(日均出库量 × 补货周期) + max(安全库存 - 当前库存, 0)`
3. **供应商管理**：分页查询、模糊搜索、软删除、外键关联校验
4. **权限控制**：Shiro + JWT 无状态认证，三级权限粒度

## 核心算法（AI 相关亮点）

```java
// 日均出库量 = 近30天出库总量 / 30（移动平均）
Integer total = outboundDao.c_selectOutboundCountByDays(consumableId, 30);
double dailyAvg = total / 30.0;

// 建议补货量 = 预测周期消耗 + 当前缺口
int suggested = (int)Math.ceil(dailyAvg * leadTimeDays)
              + Math.max(safetyCount - currentCount, 0);
```

## 目录结构

```
inventory-alarm-demo/
├── backend/          # Spring Boot 后端
├── frontend/         # Vue 前端
└── README.md
```

## 运行

见 backend 与 frontend 目录下的 README。
