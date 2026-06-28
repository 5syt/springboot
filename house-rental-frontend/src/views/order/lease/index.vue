<template>
  <div class="order-container">
    <el-card shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable @keyup.enter.native="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="租客姓名">
          <el-input v-model="searchForm.tenantName" placeholder="请输入租客姓名" clearable @keyup.enter.native="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 140px;">
            <el-option label="待签约" :value="0"></el-option>
            <el-option label="租赁中" :value="1"></el-option>
            <el-option label="已到期" :value="2"></el-option>
            <el-option label="已取消" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="table-header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增订单</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="70" align="center"></el-table-column>
        <el-table-column prop="orderNo" label="订单号" min-width="160"></el-table-column>
        <el-table-column prop="houseTitle" label="房屋" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="tenantName" label="租客" width="120"></el-table-column>
        <el-table-column prop="rent" label="租金(元/月)" width="120" align="center">
          <template slot-scope="scope">
            <span style="color: #F56C6C;">¥{{ scope.row.rent }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="startDate" label="开始日期" width="120" align="center"></el-table-column>
        <el-table-column prop="endDate" label="结束日期" width="120" align="center"></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="small" icon="el-icon-edit" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" icon="el-icon-delete" style="color: #F56C6C;" @click="handleDelete(scope.row)">删除</el-button>
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
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form ref="orderForm" :model="orderForm" :rules="rules" label-width="100px">
        <el-form-item label="订单号" prop="orderNo">
          <el-input v-model="orderForm.orderNo" placeholder="请输入订单号" :disabled="isEdit"></el-input>
        </el-form-item>
        <el-form-item label="房屋" prop="houseId">
          <el-select v-model="orderForm.houseId" placeholder="请选择房屋" filterable style="width: 100%;">
            <el-option
              v-for="item in houseList"
              :key="item.id"
              :label="item.title"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="租客姓名" prop="tenantName">
              <el-input v-model="orderForm.tenantName" placeholder="请输入租客姓名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="tenantPhone">
              <el-input v-model="orderForm.tenantPhone" placeholder="请输入联系电话"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="月租金" prop="rent">
              <el-input v-model="orderForm.rent" placeholder="请输入月租金">
                <template slot="prepend">¥</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="押金" prop="deposit">
              <el-input v-model="orderForm.deposit" placeholder="请输入押金">
                <template slot="prepend">¥</template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker
                v-model="orderForm.startDate"
                type="date"
                placeholder="选择开始日期"
                value-format="YYYY-MM-DD"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker
                v-model="orderForm.endDate"
                type="date"
                placeholder="选择结束日期"
                value-format="YYYY-MM-DD"
                style="width: 100%;"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="orderForm.status">
            <el-radio :label="0">待签约</el-radio>
            <el-radio :label="1">租赁中</el-radio>
            <el-radio :label="2">已到期</el-radio>
            <el-radio :label="3">已取消</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="orderForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOrderList, addOrder, updateOrder, deleteOrder, getOrderById } from '@/api/order'
import { getHouseList } from '@/api/house'

export default {
  name: 'LeaseOrder',
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      isEdit: false,
      houseList: [],
      searchForm: {
        orderNo: '',
        tenantName: '',
        status: '',
        dateRange: []
      },
      tableData: [],
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      orderForm: {
        id: null,
        orderNo: '',
        houseId: '',
        tenantName: '',
        tenantPhone: '',
        rent: '',
        deposit: '',
        startDate: '',
        endDate: '',
        status: 0,
        remark: ''
      },
      rules: {
        orderNo: [
          { required: true, message: '请输入订单号', trigger: 'blur' }
        ],
        houseId: [
          { required: true, message: '请选择房屋', trigger: 'change' }
        ],
        tenantName: [
          { required: true, message: '请输入租客姓名', trigger: 'blur' }
        ],
        tenantPhone: [
          { required: true, message: '请输入联系电话', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ],
        rent: [
          { required: true, message: '请输入月租金', trigger: 'blur' }
        ],
        startDate: [
          { required: true, message: '请选择开始日期', trigger: 'change' }
        ],
        endDate: [
          { required: true, message: '请选择结束日期', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑订单' : '新增订单'
    }
  },
  mounted() {
    this.loadHouseList()
    this.fetchList()
  },
  methods: {
    async loadHouseList() {
      try {
        const res = await getHouseList({ pageNum: 1, pageSize: 1000 })
        this.houseList = res.data?.list || res.data?.records || res.data || []
      } catch (error) {
        console.error('获取房屋列表失败:', error)
      }
    },
    getStatusType(status) {
      const typeMap = {
        0: 'warning',
        1: 'success',
        2: 'info',
        3: 'danger'
      }
      return typeMap[status] || 'info'
    },
    getStatusText(status) {
      const textMap = {
        0: '待签约',
        1: '租赁中',
        2: '已到期',
        3: '已取消'
      }
      return textMap[status] || '未知'
    },
    async fetchList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
        if (this.searchForm.dateRange && this.searchForm.dateRange.length === 2) {
          params.startDate = this.searchForm.dateRange[0]
          params.endDate = this.searchForm.dateRange[1]
        }
        delete params.dateRange
        const res = await getOrderList(params)
        this.tableData = res.data?.list || res.data?.records || res.data || []
        this.pagination.total = res.data?.total || 0
      } catch (error) {
        console.error('获取订单列表失败:', error)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.pageNum = 1
      this.fetchList()
    },
    handleReset() {
      this.searchForm = {
        orderNo: '',
        tenantName: '',
        status: '',
        dateRange: []
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
    handleAdd() {
      this.isEdit = false
      this.orderForm = {
        id: null,
        orderNo: '',
        houseId: '',
        tenantName: '',
        tenantPhone: '',
        rent: '',
        deposit: '',
        startDate: '',
        endDate: '',
        status: 0,
        remark: ''
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.orderForm && this.$refs.orderForm.clearValidate()
      })
    },
    async handleEdit(row) {
      this.isEdit = true
      try {
        const res = await getOrderById(row.id)
        this.orderForm = { ...res.data }
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs.orderForm && this.$refs.orderForm.clearValidate()
        })
      } catch (error) {
        this.orderForm = { ...row }
        this.dialogVisible = true
      }
    },
    async handleSubmit() {
      try {
        const valid = await this.$refs.orderForm.validate()
        if (valid) {
          this.submitLoading = true
          if (this.isEdit) {
            await updateOrder(this.orderForm)
            this.$message.success('编辑成功')
          } else {
            await addOrder(this.orderForm)
            this.$message.success('新增成功')
          }
          this.dialogVisible = false
          this.fetchList()
        }
      } catch (error) {
        if (error !== false) {
          console.error('提交失败:', error)
        }
      } finally {
        this.submitLoading = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确定要删除订单 "${row.orderNo}" 吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await deleteOrder(row.id)
        this.$message.success('删除成功')
        this.fetchList()
      } catch (e) {
        if (e !== 'cancel') {
          console.error(e)
        }
      }
    },
    handleDialogClose() {
      this.$refs.orderForm && this.$refs.orderForm.resetFields()
    }
  }
}
</script>

<style scoped>
.order-container {
  padding: 0;
}

.search-form {
  margin: 0;
}

.table-header {
  margin-bottom: 15px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  text-align: right;
}
</style>
