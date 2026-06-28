<template>
  <div class="house-info-container">
    <el-card shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" placeholder="请输入标题" clearable @keyup.enter.native="handleSearch"></el-input>
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="searchForm.typeId" placeholder="请选择类型" clearable style="width: 150px;">
            <el-option
              v-for="item in typeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable style="width: 150px;">
            <el-option label="上架" :value="1"></el-option>
            <el-option label="下架" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格区间">
          <el-input v-model="searchForm.minPrice" placeholder="最低价" style="width: 100px;"></el-input>
          <span style="margin: 0 5px;">-</span>
          <el-input v-model="searchForm.maxPrice" placeholder="最高价" style="width: 100px;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card shadow="never" style="margin-top: 20px;">
      <div class="table-header">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增房屋</el-button>
      </div>
      <el-table
        v-loading="loading"
        :data="tableData"
        border
        stripe
        style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
        <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip></el-table-column>
        <el-table-column prop="typeName" label="类型" width="120" align="center"></el-table-column>
        <el-table-column prop="area" label="面积(㎡)" width="100" align="center"></el-table-column>
        <el-table-column prop="layout" label="户型" width="120" align="center"></el-table-column>
        <el-table-column prop="rent" label="租金(元/月)" width="130" align="center">
          <template slot-scope="scope">
            <span style="color: #F56C6C; font-weight: bold;">¥{{ scope.row.rent }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="address" label="地址" min-width="200" show-overflow-tooltip></el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              active-value="1"
              inactive-value="0"
              :loading="switchLoading"
              @change="handleStatusChange(scope.row, $event)"
            ></el-switch>
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
      width="650px"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <el-form ref="houseForm" :model="houseForm" :rules="rules" label-width="100px">
        <el-form-item label="房屋标题" prop="title">
          <el-input v-model="houseForm.title" placeholder="请输入房屋标题"></el-input>
        </el-form-item>
        <el-form-item label="房屋类型" prop="typeId">
          <el-select v-model="houseForm.typeId" placeholder="请选择房屋类型" style="width: 100%;">
            <el-option
              v-for="item in typeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="面积" prop="area">
              <el-input v-model="houseForm.area" placeholder="请输入面积">
                <template slot="append">㎡</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="户型" prop="layout">
              <el-input v-model="houseForm.layout" placeholder="如：3室2厅"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="月租金" prop="rent">
              <el-input v-model="houseForm.rent" placeholder="请输入月租金">
                <template slot="prepend">¥</template>
                <template slot="append">元/月</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="楼层" prop="floor">
              <el-input v-model="houseForm.floor" placeholder="如：3/6层"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址" prop="address">
          <el-input v-model="houseForm.address" placeholder="请输入详细地址"></el-input>
        </el-form-item>
        <el-form-item label="房屋图片" prop="images">
          <el-upload
            class="image-uploader"
            :action="uploadUrl"
            :headers="uploadHeaders"
            list-type="picture-card"
            :file-list="imageList"
            :on-success="handleUploadSuccess"
            :on-remove="handleUploadRemove"
            :before-upload="beforeUpload"
            multiple
            :limit="9"
            accept="image/*"
          >
            <i class="el-icon-plus"></i>
            <div class="el-upload__text">上传图片</div>
          </el-upload>
        </el-form-item>
        <el-form-item label="房屋描述" prop="description">
          <el-input
            v-model="houseForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入房屋描述"
          ></el-input>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="houseForm.status">
            <el-radio :label="1">上架</el-radio>
            <el-radio :label="0">下架</el-radio>
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
import { getHouseList, addHouse, updateHouse, deleteHouse, updateHouseStatus, getHouseById } from '@/api/house'
import { getHouseTypeAll } from '@/api/houseType'
import { getToken } from '@/utils/auth'

export default {
  name: 'HouseInfo',
  data() {
    return {
      loading: false,
      submitLoading: false,
      switchLoading: false,
      dialogVisible: false,
      isEdit: false,
      typeList: [],
      imageList: [],
      uploadUrl: '/api/house/upload',
      searchForm: {
        title: '',
        typeId: '',
        status: '',
        minPrice: '',
        maxPrice: ''
      },
      tableData: [],
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      houseForm: {
        id: null,
        title: '',
        typeId: '',
        area: '',
        layout: '',
        rent: '',
        floor: '',
        address: '',
        images: '',
        description: '',
        status: 1
      },
      rules: {
        title: [
          { required: true, message: '请输入房屋标题', trigger: 'blur' },
          { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
        ],
        typeId: [
          { required: true, message: '请选择房屋类型', trigger: 'change' }
        ],
        area: [
          { required: true, message: '请输入面积', trigger: 'blur' }
        ],
        layout: [
          { required: true, message: '请输入户型', trigger: 'blur' }
        ],
        rent: [
          { required: true, message: '请输入月租金', trigger: 'blur' }
        ],
        address: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '请选择状态', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    dialogTitle() {
      return this.isEdit ? '编辑房屋' : '新增房屋'
    },
    uploadHeaders() {
      return {
        'Authorization': 'Bearer ' + getToken()
      }
    }
  },
  mounted() {
    this.loadTypeList()
    this.fetchList()
  },
  methods: {
    loadTypeList() {
      const self = this
      getHouseTypeAll().then(function(res) {
        self.typeList = res.data || []
      }).catch(function(error) {
        console.error('获取房屋类型列表失败:', error)
      })
    },
    fetchList() {
      const self = this
      this.loading = true
      const params = {
        title: this.searchForm.title,
        typeId: this.searchForm.typeId,
        status: this.searchForm.status,
        minPrice: this.searchForm.minPrice,
        maxPrice: this.searchForm.maxPrice,
        pageNum: this.pagination.pageNum,
        pageSize: this.pagination.pageSize
      }
      getHouseList(params).then(function(res) {
        self.tableData = (res.data && res.data.records) || (res.data && res.data.list) || res.data || []
        self.pagination.total = (res.data && res.data.total) || 0
        self.loading = false
      }).catch(function(error) {
        console.error('获取房屋列表失败:', error)
        self.loading = false
      })
    },
    handleSearch() {
      this.pagination.pageNum = 1
      this.fetchList()
    },
    handleReset() {
      this.searchForm = {
        title: '',
        typeId: '',
        status: '',
        minPrice: '',
        maxPrice: ''
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
      this.houseForm = {
        id: null,
        title: '',
        typeId: '',
        area: '',
        layout: '',
        rent: '',
        floor: '',
        address: '',
        images: '',
        description: '',
        status: 1
      }
      this.imageList = []
      this.dialogVisible = true
      const self = this
      this.$nextTick(function() {
        if (self.$refs.houseForm) {
          self.$refs.houseForm.clearValidate()
        }
      })
    },
    handleEdit(row) {
      const self = this
      this.isEdit = true
      getHouseById(row.id).then(function(res) {
        self.houseForm = Object.assign({}, res.data)
        if (res.data && res.data.images) {
          const images = res.data.images.split(',')
          self.imageList = images.map(function(url, index) {
            return { name: '图片' + (index + 1), url: url }
          })
        } else {
          self.imageList = []
        }
        self.dialogVisible = true
        self.$nextTick(function() {
          if (self.$refs.houseForm) {
            self.$refs.houseForm.clearValidate()
          }
        })
      }).catch(function(error) {
        self.houseForm = Object.assign({}, row)
        self.imageList = []
        self.dialogVisible = true
      })
    },
    handleSubmit() {
      const self = this
      this.$refs.houseForm.validate(function(valid) {
        if (valid) {
          self.submitLoading = true
          const api = self.isEdit ? updateHouse : addHouse
          api(self.houseForm).then(function() {
            self.$message.success(self.isEdit ? '编辑成功' : '新增成功')
            self.dialogVisible = false
            self.fetchList()
            self.submitLoading = false
          }).catch(function(error) {
            if (error !== false) {
              console.error('提交失败:', error)
            }
            self.submitLoading = false
          })
        }
      })
    },
    handleDelete(row) {
      const self = this
      this.$confirm('确定要删除房屋 "' + row.title + '" 吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(function() {
        return deleteHouse(row.id)
      }).then(function() {
        self.$message.success('删除成功')
        self.fetchList()
      }).catch(function(e) {
        if (e !== 'cancel') {
          console.error(e)
        }
      })
    },
    handleStatusChange(row, val) {
      const self = this
      updateHouseStatus(row.id).then(function() {
        self.$message.success(val === '1' ? '已上架' : '已下架')
      }).catch(function(error) {
        row.status = row.status === '1' ? '0' : '1'
        console.error('状态切换失败:', error)
      })
    },
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/')
      const isLt5M = file.size / 1024 / 1024 < 5
      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }
      if (!isLt5M) {
        this.$message.error('图片大小不能超过 5MB!')
        return false
      }
      return true
    },
    handleUploadSuccess(response, file) {
      if (response.code === 200) {
        const url = response.data
        if (!this.houseForm.images) {
          this.houseForm.images = url
        } else {
          this.houseForm.images += ',' + url
        }
        this.$message.success('上传成功')
      } else {
        this.$message.error(response.message || '上传失败')
        const index = this.imageList.indexOf(file)
        if (index > -1) {
          this.imageList.splice(index, 1)
        }
      }
    },
    handleUploadRemove(file) {
      const url = file.url
      const images = this.houseForm.images.split(',')
      const index = images.indexOf(url)
      if (index > -1) {
        images.splice(index, 1)
        this.houseForm.images = images.join(',')
      }
    },
    handleDialogClose() {
      if (this.$refs.houseForm) {
        this.$refs.houseForm.resetFields()
      }
      this.imageList = []
    }
  }
}
</script>

<style scoped>
.house-info-container {
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

.image-uploader >>> .el-upload--picture-card {
  width: 100px;
  height: 100px;
  line-height: 100px;
}

.image-uploader >>> .el-upload-list--picture-card .el-upload-list__item {
  width: 100px;
  height: 100px;
}
</style>
