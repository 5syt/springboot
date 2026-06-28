<template>
  <div class="house-type-container">
    <el-card shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="类型名称">
          <el-input v-model="searchForm.name" placeholder="请输入类型名称" clearable @keyup.enter.native="handleSearch"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="table-header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增类型</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="name" label="类型名称" min-width="150"></el-table-column>
        <el-table-column prop="description" label="描述" min-width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="sort" label="排序" width="100" align="center"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" min-width="160"></el-table-column>
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
      width="500px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form ref="typeForm" :model="typeForm" :rules="rules" label-width="80px">
        <el-form-item label="类型名称" prop="name">
          <el-input v-model="typeForm.name" placeholder="请输入类型名称"></el-input>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="typeForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入描述"
          ></el-input>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="typeForm.sort" :min="0" :max="9999" style="width: 100%;"></el-input-number>
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
import { getHouseTypeList, addHouseType, updateHouseType, deleteHouseType, getHouseTypeById } from '@/api/houseType'

export default {
  name: 'HouseType',
  data() {
    return {
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      isEdit: false,
      searchForm: {
        name: ''
      },
      tableData: [],
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      typeForm: {
        id: null,
        name: '',
        description: '',
        sort: 0
      },
      rules: {
        name: [
          { required: true, message: '请输入类型名称', trigger: 'blur' },
          { min: 1, max: 50, message: '长度在 1 到 50 个字符', trigger: 'blur' }
        ],
        description: [
          { max: 500, message: '描述不能超过500个字符', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入排序号', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑房屋类型' : '新增房屋类型'
    }
  },
  mounted() {
    this.fetchList()
  },
  methods: {
    async fetchList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
        const res = await getHouseTypeList(params)
        this.tableData = res.data?.list || res.data?.records || res.data || []
        this.pagination.total = res.data?.total || 0
      } catch (error) {
        console.error('获取房屋类型列表失败:', error)
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
        name: ''
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
      this.typeForm = {
        id: null,
        name: '',
        description: '',
        sort: 0
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.typeForm && this.$refs.typeForm.clearValidate()
      })
    },
    async handleEdit(row) {
      this.isEdit = true
      try {
        const res = await getHouseTypeById(row.id)
        this.typeForm = { ...res.data }
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs.typeForm && this.$refs.typeForm.clearValidate()
        })
      } catch (error) {
        this.typeForm = { ...row }
        this.dialogVisible = true
      }
    },
    async handleSubmit() {
      try {
        const valid = await this.$refs.typeForm.validate()
        if (valid) {
          this.submitLoading = true
          if (this.isEdit) {
            await updateHouseType(this.typeForm)
            this.$message.success('编辑成功')
          } else {
            await addHouseType(this.typeForm)
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
        await this.$confirm(`确定要删除房屋类型 "${row.name}" 吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await deleteHouseType(row.id)
        this.$message.success('删除成功')
        this.fetchList()
      } catch (e) {
        if (e !== 'cancel') {
          console.error(e)
        }
      }
    },
    handleDialogClose() {
      this.$refs.typeForm && this.$refs.typeForm.resetFields()
    }
  }
}
</script>

<style scoped>
.house-type-container {
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
