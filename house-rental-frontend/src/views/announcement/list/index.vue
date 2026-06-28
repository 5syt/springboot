<template>
  <div class="announcement-container">
    <el-card shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable @keyup.enter.native="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择类型" clearable style="width: 140px;">
            <el-option label="系统公告" :value="1"></el-option>
            <el-option label="活动通知" :value="2"></el-option>
            <el-option label="其他" :value="3"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 140px;">
            <el-option label="已发布" :value="1"></el-option>
            <el-option label="已下架" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="table-header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增公告</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="70" align="center"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="250" show-overflow-tooltip></el-table-column>
        <el-table-column prop="type" label="类型" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getTypeTagType(scope.row.type)" size="small">
              {{ getTypeText(scope.row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="1"
              inactive-value="0"
              :loading="switchLoading"
              active-text="发布"
              inactive-text="下架"
              @change="(val) => handleStatusChange(scope.row, val)"
            ></el-switch>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" align="center"></el-table-column>
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
      <el-form ref="announcementForm" :model="announcementForm" :rules="rules" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="announcementForm.title" placeholder="请输入公告标题"></el-input>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="announcementForm.type">
            <el-radio :label="1">系统公告</el-radio>
            <el-radio :label="2">活动通知</el-radio>
            <el-radio :label="3">其他</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="announcementForm.content"
            type="textarea"
            :rows="10"
            placeholder="请输入公告内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="announcementForm.status">
            <el-radio :label="1">已发布</el-radio>
            <el-radio :label="0">已下架</el-radio>
          </el-radio-group>
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
import { getAnnouncementList, addAnnouncement, updateAnnouncement, deleteAnnouncement, updateAnnouncementStatus, getAnnouncementById } from '@/api/announcement'

export default {
  name: 'AnnouncementList',
  data() {
    return {
      loading: false,
      submitLoading: false,
      switchLoading: false,
      dialogVisible: false,
      isEdit: false,
      searchForm: {
        title: '',
        type: '',
        status: ''
      },
      tableData: [],
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      announcementForm: {
        id: null,
        title: '',
        type: 1,
        content: '',
        status: 1
      },
      rules: {
        title: [
          { required: true, message: '请输入公告标题', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择类型', trigger: 'change' }
        ],
        content: [
          { required: true, message: '请输入公告内容', trigger: 'blur' },
          { min: 5, max: 5000, message: '长度在 5 到 5000 个字符', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑公告' : '新增公告'
    }
  },
  mounted() {
    this.fetchList()
  },
  methods: {
    getTypeText(type) {
      const textMap = {
        1: '系统公告',
        2: '活动通知',
        3: '其他'
      }
      return textMap[type] || '其他'
    },
    getTypeTagType(type) {
      const typeMap = {
        1: 'primary',
        2: 'success',
        3: 'info'
      }
      return typeMap[type] || 'info'
    },
    async fetchList() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize
        }
        const res = await getAnnouncementList(params)
        this.tableData = res.data?.list || res.data?.records || res.data || []
        this.pagination.total = res.data?.total || 0
      } catch (error) {
        console.error('获取公告列表失败:', error)
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
        title: '',
        type: '',
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
    handleAdd() {
      this.isEdit = false
      this.announcementForm = {
        id: null,
        title: '',
        type: 1,
        content: '',
        status: 1
      }
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs.announcementForm && this.$refs.announcementForm.clearValidate()
      })
    },
    async handleEdit(row) {
      this.isEdit = true
      try {
        const res = await getAnnouncementById(row.id)
        this.announcementForm = { ...res.data }
        this.dialogVisible = true
        this.$nextTick(() => {
          this.$refs.announcementForm && this.$refs.announcementForm.clearValidate()
        })
      } catch (error) {
        this.announcementForm = { ...row }
        this.dialogVisible = true
      }
    },
    async handleSubmit() {
      try {
        const valid = await this.$refs.announcementForm.validate()
        if (valid) {
          this.submitLoading = true
          if (this.isEdit) {
            await updateAnnouncement(this.announcementForm)
            this.$message.success('编辑成功')
          } else {
            await addAnnouncement(this.announcementForm)
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
        await this.$confirm(`确定要删除公告 "${row.title}" 吗?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await deleteAnnouncement(row.id)
        this.$message.success('删除成功')
        this.fetchList()
      } catch (e) {
        if (e !== 'cancel') {
          console.error(e)
        }
      }
    },
    async handleStatusChange(row, val) {
      try {
        await updateAnnouncementStatus(row.id, val)
        this.$message.success(val === '1' ? '已发布' : '已下架')
      } catch (error) {
        row.status = row.status === '1' ? '0' : '1'
        console.error('状态切换失败:', error)
      }
    },
    handleDialogClose() {
      this.$refs.announcementForm && this.$refs.announcementForm.resetFields()
    }
  }
}
</script>

<style scoped>
.announcement-container {
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
