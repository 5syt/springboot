<template>
  <div class="my-order-container">
    <el-card shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.keyword" placeholder="请输入订单号" clearable @keyup.enter.native="handleSearch" style="width: 180px;"></el-input>
        </el-form-item>
        <el-form-item label="订单状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 140px;">
            <el-option label="待审核" :value="0"></el-option>
            <el-option label="租赁中" :value="1"></el-option>
            <el-option label="已到期" :value="2"></el-option>
            <el-option label="已取消" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="list-header">
        <span class="total-text">共 <b>{{ pagination.total }}</b> 条订单记录</span>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%;"
      >
        <el-table-column prop="orderNo" label="订单编号" min-width="160"></el-table-column>
        <el-table-column prop="houseTitle" label="房屋信息" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="monthRent" label="月租金" width="120" align="center">
          <template slot-scope="scope">
            <span style="color: #F56C6C; font-weight: bold;">¥{{ scope.row.monthRent }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="入住日期" width="120" align="center"></el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="120" align="center"></el-table-column>
        <el-table-column label="租期" width="90" align="center">
          <template slot-scope="scope">
            {{ calculateLeaseMonths(scope.row.startDate, scope.row.endDate) }}个月
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="160" align="center"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-view" @click="handleViewDetail(scope.row)">详情</el-button>
            <el-button
              v-if="scope.row.status === 0"
              type="text"
              size="small"
              icon="el-icon-close"
              style="color: #F56C6C;"
              @click="handleCancel(scope.row)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-wrapper">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.pageNum"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
          background
        ></el-pagination>
      </div>
    </el-card>

    <el-dialog
      title="订单详情"
      :visible.sync="detailVisible"
      width="550px"
      :close-on-click-modal="false"
    >
      <div v-if="currentOrder" class="order-detail">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="订单编号">
            <span style="font-weight: bold;">{{ currentOrder.orderNo }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(currentOrder.status)" size="small">
              {{ getStatusText(currentOrder.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="房屋信息">
            {{ currentOrder.houseTitle || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="租客姓名">
            {{ currentOrder.tenantName || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="联系电话">
            {{ currentOrder.tenantPhone || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="月租金">
            <span style="color: #F56C6C; font-weight: bold;">¥{{ currentOrder.monthRent }}</span> 元/月
          </el-descriptions-item>
          <el-descriptions-item label="入住日期">
            {{ currentOrder.startDate || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="结束日期">
            {{ currentOrder.endDate || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="租期">
            {{ calculateLeaseMonths(currentOrder.startDate, currentOrder.endDate) }} 个月
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">
            {{ currentOrder.createTime || '-' }}
          </el-descriptions-item>
          <el-descriptions-item label="备注" v-if="currentOrder.remark">
            {{ currentOrder.remark }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getMyOrderList, updateOrderStatus, getOrderById } from '@/api/order'

export default {
  name: 'MyOrderList',
  data() {
    return {
      loading: false,
      tableData: [],
      searchForm: {
        keyword: '',
        status: ''
      },
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      detailVisible: false,
      currentOrder: null
    }
  },
  mounted() {
    this.fetchList()
  },
  methods: {
    fetchList() {
      const self = this
      this.loading = true
      const params = {
        pageNum: this.pagination.pageNum,
        pageSize: this.pagination.pageSize
      }
      if (this.searchForm.status !== '' && this.searchForm.status !== null && this.searchForm.status !== undefined) {
        params.status = this.searchForm.status
      }
      getMyOrderList(params).then(function(res) {
        const data = res.data || {}
        self.tableData = (data.records || data.list || [])
        self.pagination.total = data.total || 0
        self.loading = false
      }).catch(function(error) {
        console.error('获取订单列表失败:', error)
        self.loading = false
      })
    },
    handleSearch() {
      this.pagination.pageNum = 1
      this.fetchList()
    },
    handleReset() {
      this.searchForm = {
        keyword: '',
        status: ''
      }
      this.pagination.pageNum = 1
      this.fetchList()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.pagination.pageNum = 1
      this.fetchList()
    },
    handleCurrentChange(val) {
      this.pagination.pageNum = val
      this.fetchList()
    },
    getStatusText(status) {
      const map = {
        0: '待审核',
        1: '租赁中',
        2: '已到期',
        3: '已取消'
      }
      return map[status] || '未知'
    },
    calculateLeaseMonths(startDate, endDate) {
      if (!startDate || !endDate) return '-'
      const start = new Date(startDate)
      const end = new Date(endDate)
      let months = (end.getFullYear() - start.getFullYear()) * 12 + (end.getMonth() - start.getMonth())
      if (end.getDate() >= start.getDate()) {
        months += 1
      }
      return months > 0 ? months : '-'
    },
    getStatusType(status) {
      const map = {
        0: 'warning',
        1: 'success',
        2: 'info',
        3: 'danger'
      }
      return map[status] || 'info'
    },
    handleViewDetail(row) {
      const self = this
      getOrderById(row.id).then(function(res) {
        self.currentOrder = res.data || row
        self.detailVisible = true
      }).catch(function() {
        self.currentOrder = row
        self.detailVisible = true
      })
    },
    handleCancel(row) {
      const self = this
      this.$confirm('确定要取消该订单申请吗?', '提示', {
        confirmButtonText: '确定取消',
        cancelButtonText: '再想想',
        type: 'warning'
      }).then(function() {
        return updateOrderStatus(row.id, 3)
      }).then(function() {
        self.$message.success('订单已取消')
        self.fetchList()
      }).catch(function(e) {
        if (e !== 'cancel') {
          console.error(e)
        }
      })
    }
  }
}
</script>

<style scoped>
.my-order-container {
  padding: 0;
}

.search-form {
  margin: 0;
}

.list-header {
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.total-text {
  color: #606266;
  font-size: 14px;
}

.total-text b {
  color: #409EFF;
  font-size: 16px;
  margin: 0 3px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.order-detail {
  padding: 10px 0;
}

.dialog-footer {
  text-align: right;
}
</style>
