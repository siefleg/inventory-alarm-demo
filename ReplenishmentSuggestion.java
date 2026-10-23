/**
 * 库存预警与补货建议核心算法演示（课程 Demo）
 * 移动平均法预测日均出库量 + 安全库存约束计算补货量
 */
public class ReplenishmentSuggestion {

    private static final int STATISTICS_DAYS = 30;   // 统计窗口：近30天
    private static final int DEFAULT_LEAD_TIME = 7;  // 默认补货周期：7天

    private final String name;
    private final int currentCount;
    private final int safetyCount;
    private final int outboundTotal;   // 近30天出库总量（来自数据库查询）

    public ReplenishmentSuggestion(String name, int currentCount,
                                   int safetyCount, int outboundTotal) {
        this.name = name;
        this.currentCount = currentCount;
        this.safetyCount = safetyCount;
        this.outboundTotal = outboundTotal;
    }

    /** 日均出库量 = 近30天出库总量 / 30 */
    public double getDailyAverageOutbound() {
        return outboundTotal / (double) STATISTICS_DAYS;
    }

    /** 缺货缺口 = max(安全库存 - 当前库存, 0) */
    public int getShortage() {
        return Math.max(safetyCount - currentCount, 0);
    }

    /**
     * 建议补货量 = ceil(日均出库量 × 补货周期) + 缺货缺口
     */
    public int suggest(int leadTimeDays) {
        int days = leadTimeDays > 0 ? leadTimeDays : DEFAULT_LEAD_TIME;
        int predictedConsumption = (int) Math.ceil(getDailyAverageOutbound() * days);
        return predictedConsumption + getShortage();
    }

    /** 紧急程度：高危 / 中度 / 正常 */
    public String getUrgencyLevel() {
        if (currentCount <= 0 || currentCount <= safetyCount / 2.0) {
            return "high";
        } else if (currentCount <= safetyCount) {
            return "moderate";
        }
        return "low";
    }

    @Override
    public String toString() {
        return String.format(
            "%s: 当前=%d 安全线=%d 日均出库=%.2f 建议补货=%d 紧急度=%s",
            name, currentCount, safetyCount,
            getDailyAverageOutbound(), suggest(DEFAULT_LEAD_TIME), getUrgencyLevel());
    }

    public static void main(String[] args) {
        // 演示：签字笔，当前库存200，安全线500，近30天出库900支
        ReplenishmentSuggestion pen =
            new ReplenishmentSuggestion("签字笔", 200, 500, 900);
        System.out.println(pen);
        // 预期输出：
        // 签字笔: 当前=200 安全线=500 日均出库=30.00 建议补货=510 紧急度=high
    }
}
