<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :xs="12" :sm="12" :md="6" :lg="6" v-for="(item, index) in statCards" :key="index">
        <el-card class="stat-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-icon" :style="{ background: item.color }">
              <i :class="item.icon"></i>
            </div>
            <div class="stat-info">
              <div class="stat-value">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card title="房屋类型分布" shadow="hover">
          <div v-loading="pieLoading" class="chart-container" ref="pieChart"></div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card title="月度订单趋势" shadow="hover">
          <div v-loading="lineLoading" class="chart-container" ref="lineChart"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import echarts from 'echarts'
import { getStatistics, getHouseTypeStatistics, getOrderTrendStatistics } from '@/api/statistics'

export default {
  name: 'Dashboard',
  data() {
    return {
      pieLoading: false,
      lineLoading: false,
      pieChart: null,
      lineChart: null,
      statCards: [
        { label: '房屋总数', value: 0, icon: 'el-icon-office-building', color: '#409EFF' },
        { label: '在租房源', value: 0, icon: 'el-icon-house', color: '#67C23A' },
        { label: '订单总数', value: 0, icon: 'el-icon-s-order', color: '#E6A23C' },
        { label: '本月新增订单', value: 0, icon: 'el-icon-s-data', color: '#F56C6C' }
      ],
      pieOption: {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '房屋类型',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '16',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: []
          }
        ],
        color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#9b59b6', '#1abc9c']
      },
      lineOption: {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['订单数量']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '订单数量',
            type: 'line',
            smooth: true,
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
                ]
              }
            },
            lineStyle: {
              color: '#409EFF',
              width: 2
            },
            itemStyle: {
              color: '#409EFF'
            },
            data: []
          }
        ]
      }
    }
  },
  mounted() {
    this.initCharts()
    this.loadStatistics()
    this.loadHouseTypeStatistics()
    this.loadOrderTrendStatistics()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.pieChart) {
      this.pieChart.dispose()
    }
    if (this.lineChart) {
      this.lineChart.dispose()
    }
  },
  methods: {
    initCharts() {
      this.$nextTick(() => {
        if (this.$refs.pieChart) {
          this.pieChart = echarts.init(this.$refs.pieChart)
          this.pieChart.setOption(this.pieOption)
        }
        if (this.$refs.lineChart) {
          this.lineChart = echarts.init(this.$refs.lineChart)
          this.lineChart.setOption(this.lineOption)
        }
      })
    },
    handleResize() {
      if (this.pieChart) {
        this.pieChart.resize()
      }
      if (this.lineChart) {
        this.lineChart.resize()
      }
    },
    async loadStatistics() {
      try {
        const res = await getStatistics()
        const data = res.data || {}
        this.statCards[0].value = data.houseTotal || 0
        this.statCards[1].value = data.houseRenting || 0
        this.statCards[2].value = data.orderTotal || 0
        this.statCards[3].value = data.monthNewOrders || 0
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    async loadHouseTypeStatistics() {
      this.pieLoading = true
      try {
        const res = await getHouseTypeStatistics()
        const data = res.data || []
        this.pieOption.series[0].data = data
        if (this.pieChart) {
          this.pieChart.setOption(this.pieOption)
        }
      } catch (error) {
        console.error('获取房屋类型统计失败:', error)
      } finally {
        this.pieLoading = false
      }
    },
    async loadOrderTrendStatistics() {
      this.lineLoading = true
      try {
        const res = await getOrderTrendStatistics()
        const data = res.data || {}
        const months = data.months || []
        const counts = data.counts || []
        this.lineOption.series[0].data = counts
        if (months.length > 0) {
          this.lineOption.xAxis.data = months
        }
        if (this.lineChart) {
          this.lineChart.setOption(this.lineOption)
        }
      } catch (error) {
        console.error('获取订单趋势统计失败:', error)
      } finally {
        this.lineLoading = false
      }
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 10px;
}

.stat-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  flex-shrink: 0;
}

.stat-icon i {
  font-size: 28px;
  color: #fff;
}

.stat-info {
  flex: 1;
  min-width: 0;
}

.stat-value {
  font-size: 26px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 6px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.chart-container {
  width: 100%;
  height: 350px;
}
</style>
