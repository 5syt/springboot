<template>
  <div class="tenant-house-container">
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="请输入标题/地址" clearable @keyup.enter.native="handleSearch" style="width: 200px;"></el-input>
        </el-form-item>
        <el-form-item label="房屋类型">
          <el-select v-model="searchForm.typeId" placeholder="全部类型" clearable style="width: 150px;">
            <el-option
              v-for="item in typeList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格区间">
          <el-input v-model="searchForm.minPrice" placeholder="最低价" style="width: 100px;"></el-input>
          <span style="margin: 0 8px; color: #909399;">-</span>
          <el-input v-model="searchForm.maxPrice" placeholder="最高价" style="width: 100px;"></el-input>
          <span style="margin-left: 5px; color: #909399;">元/月</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <div class="house-list-wrapper" v-loading="loading">
      <div v-if="houseList.length === 0 && !loading" class="empty-tip">
        <i class="el-icon-house empty-icon"></i>
        <p>暂无符合条件的房屋</p>
      </div>
      <div class="house-card-list">
        <div
          v-for="item in houseList"
          :key="item.id"
          class="house-card"
          @click="handleViewDetail(item)"
        >
          <div class="house-image">
            <img :src="getFirstImage(item.images)" :alt="item.title" />
            <div class="house-price-tag">
              <span class="price-symbol">¥</span>
              <span class="price-num">{{ item.rentPrice }}</span>
              <span class="price-unit">/月</span>
            </div>
          </div>
          <div class="house-info">
            <h3 class="house-title" :title="item.title">{{ item.title }}</h3>
            <div class="house-meta">
              <span class="meta-item">
                <i class="el-icon-house"></i>
                {{ item.rooms || '暂无' }}
              </span>
              <span class="meta-item">
                <i class="el-icon-rank"></i>
                {{ item.area || 0 }}㎡
              </span>
              <span class="meta-item" v-if="item.floor">
                <i class="el-icon-location-outline"></i>
                {{ item.floor }}
              </span>
            </div>
            <div class="house-address" :title="item.address">
              <i class="el-icon-location"></i>
              {{ item.address || '暂无地址' }}
            </div>
            <div class="house-card-footer">
              <el-tag size="small" type="info" v-if="item.typeName">{{ item.typeName }}</el-tag>
              <el-button type="primary" size="small" icon="el-icon-phone" @click.stop="handleApply(item)">
                申请租赁
              </el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="pagination-wrapper" v-if="houseList.length > 0">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.pageNum"
        :page-sizes="[8, 12, 20, 30]"
        :page-size="pagination.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        background
      ></el-pagination>
    </div>

    <el-dialog
      title="房屋详情"
      :visible.sync="detailVisible"
      width="700px"
      :close-on-click-modal="false"
      class="detail-dialog"
    >
      <div v-if="currentHouse" class="house-detail">
        <div class="detail-images">
          <el-carousel :interval="3000" height="300px">
            <el-carousel-item v-for="(img, index) in getImageList(currentHouse.images)" :key="index">
              <img :src="img" :alt="currentHouse.title" class="detail-img" />
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="detail-content">
          <h2 class="detail-title">{{ currentHouse.title }}</h2>
          <div class="detail-price">
            <span class="price-symbol">¥</span>
            <span class="price-num">{{ currentHouse.rentPrice }}</span>
            <span class="price-unit">元/月</span>
          </div>
          <el-divider></el-divider>
          <div class="detail-info-grid">
            <div class="info-item">
              <span class="info-label">房屋户型</span>
              <span class="info-value">{{ currentHouse.rooms || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">建筑面积</span>
              <span class="info-value">{{ currentHouse.area || 0 }}㎡</span>
            </div>
            <div class="info-item">
              <span class="info-label">房屋楼层</span>
              <span class="info-value">{{ currentHouse.floor || '暂无' }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">房屋类型</span>
              <span class="info-value">{{ currentHouse.typeName || '暂无' }}</span>
            </div>
          </div>
          <div class="detail-address">
            <span class="info-label"><i class="el-icon-location"></i> 详细地址：</span>
            <span class="info-value">{{ currentHouse.address || '暂无' }}</span>
          </div>
          <div class="detail-description" v-if="currentHouse.description">
            <div class="info-label">房屋描述</div>
            <div class="desc-content">{{ currentHouse.description }}</div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false">关 闭</el-button>
        <el-button type="primary" icon="el-icon-phone" @click="handleApplyFromDetail">
          申请租赁
        </el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="申请租赁"
      :visible.sync="applyVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="applyForm" :model="applyForm" :rules="applyRules" label-width="100px">
        <el-form-item label="房屋信息">
          <span style="color: #303133; font-weight: bold;">{{ currentHouse ? currentHouse.title : '' }}</span>
        </el-form-item>
        <el-form-item label="月租金">
          <span style="color: #F56C6C; font-weight: bold;">¥{{ currentHouse ? currentHouse.rentPrice : 0 }} 元/月</span>
        </el-form-item>
        <el-form-item label="入住日期" prop="startDate">
          <el-date-picker
            v-model="applyForm.startDate"
            type="date"
            placeholder="请选择入住日期"
            value-format="yyyy-MM-dd"
            style="width: 100%;"
            :picker-options="startDatePickerOptions"
          ></el-date-picker>
        </el-form-item>
        <el-form-item label="租期(月)" prop="leaseMonths">
          <el-input-number
            v-model="applyForm.leaseMonths"
            :min="1"
            :max="60"
            style="width: 100%;"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="applyForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息（选填）"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="applyVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmitApply">
          提交申请
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getHouseList, getHouseById } from '@/api/house'
import { getHouseTypeAll } from '@/api/houseType'
import { applyLease } from '@/api/order'

export default {
  name: 'TenantHouseList',
  data() {
    return {
      loading: false,
      submitLoading: false,
      typeList: [],
      houseList: [],
      searchForm: {
        keyword: '',
        typeId: '',
        minPrice: '',
        maxPrice: ''
      },
      pagination: {
        pageNum: 1,
        pageSize: 8,
        total: 0
      },
      detailVisible: false,
      applyVisible: false,
      currentHouse: null,
      applyForm: {
        houseId: null,
        startDate: '',
        leaseMonths: 12,
        remark: ''
      },
      applyRules: {
        startDate: [
          { required: true, message: '请选择入住日期', trigger: 'change' }
        ],
        leaseMonths: [
          { required: true, message: '请输入租期', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    startDatePickerOptions() {
      const self = this
      return {
        disabledDate(time) {
          return time.getTime() < Date.now() - 24 * 60 * 60 * 1000
        }
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
        keyword: this.searchForm.keyword,
        typeId: this.searchForm.typeId || undefined,
        minPrice: this.searchForm.minPrice || undefined,
        maxPrice: this.searchForm.maxPrice || undefined,
        status: 1,
        pageNum: this.pagination.pageNum,
        pageSize: this.pagination.pageSize
      }
      getHouseList(params).then(function(res) {
        const data = res.data || {}
        self.houseList = (data.records || data.list || [])
        self.pagination.total = data.total || 0
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
        keyword: '',
        typeId: '',
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
    getFirstImage(images) {
      if (!images) {
        return 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20apartment%20exterior%20building%20house&image_size=square'
      }
      const list = images.split(',')
      return list[0] || 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20apartment%20exterior%20building%20house&image_size=square'
    },
    getImageList(images) {
      if (!images) {
        return ['https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=modern%20apartment%20interior%20living%20room&image_size=landscape_16_9']
      }
      return images.split(',').filter(function(img) { return img })
    },
    handleViewDetail(item) {
      const self = this
      getHouseById(item.id).then(function(res) {
        self.currentHouse = res.data || item
        self.detailVisible = true
      }).catch(function() {
        self.currentHouse = item
        self.detailVisible = true
      })
    },
    handleApply(item) {
      this.currentHouse = item
      this.applyForm.houseId = item.id
      this.applyForm.startDate = ''
      this.applyForm.leaseMonths = 12
      this.applyForm.remark = ''
      this.applyVisible = true
      const self = this
      this.$nextTick(function() {
        if (self.$refs.applyForm) {
          self.$refs.applyForm.clearValidate()
        }
      })
    },
    handleApplyFromDetail() {
      if (!this.currentHouse) return
      this.detailVisible = false
      this.handleApply(this.currentHouse)
    },
    handleSubmitApply() {
      const self = this
      this.$refs.applyForm.validate(function(valid) {
        if (valid) {
          self.submitLoading = true
          const submitData = {
            houseId: self.applyForm.houseId,
            startDate: self.applyForm.startDate,
            leaseMonths: self.applyForm.leaseMonths,
            remark: self.applyForm.remark
          }
          applyLease(submitData).then(function() {
            self.$message.success('申请提交成功，请等待管理员审核')
            self.applyVisible = false
            self.submitLoading = false
          }).catch(function(error) {
            if (error !== false) {
              console.error('提交申请失败:', error)
            }
            self.submitLoading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>
.tenant-house-container {
  padding: 0;
}

.search-card {
  margin-bottom: 20px;
}

.search-form {
  margin: 0;
}

.house-list-wrapper {
  min-height: 300px;
}

.empty-tip {
  text-align: center;
  padding: 80px 0;
  color: #909399;
}

.empty-icon {
  font-size: 64px;
  display: block;
  margin-bottom: 20px;
  color: #dcdfe6;
}

.house-card-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.house-card {
  width: calc(25% - 15px);
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: all 0.3s;
}

.house-card:hover {
  box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
  transform: translateY(-3px);
}

.house-image {
  position: relative;
  width: 100%;
  height: 180px;
  overflow: hidden;
  background: #f5f7fa;
}

.house-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}

.house-price-tag {
  position: absolute;
  left: 0;
  bottom: 0;
  background: rgba(245, 108, 108, 0.9);
  color: #fff;
  padding: 4px 12px;
  border-top-right-radius: 8px;
}

.price-symbol {
  font-size: 12px;
}

.price-num {
  font-size: 18px;
  font-weight: bold;
}

.price-unit {
  font-size: 12px;
}

.house-info {
  padding: 12px 16px 16px;
}

.house-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 10px 0;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.house-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-bottom: 8px;
  color: #606266;
  font-size: 13px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.house-address {
  color: #909399;
  font-size: 13px;
  margin-bottom: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  display: flex;
  align-items: center;
  gap: 4px;
}

.house-card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 10px;
  border-top: 1px solid #ebeef5;
}

.pagination-wrapper {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

.detail-dialog /deep/ .el-dialog__body {
  padding: 0 20px 20px;
}

.house-detail {
  padding: 0;
}

.detail-images {
  margin: 0 -20px 20px;
}

.detail-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.detail-title {
  font-size: 22px;
  font-weight: bold;
  color: #303133;
  margin: 0 0 12px 0;
}

.detail-price {
  color: #F56C6C;
  display: flex;
  align-items: baseline;
}

.detail-price .price-symbol {
  font-size: 16px;
}

.detail-price .price-num {
  font-size: 28px;
  font-weight: bold;
  margin: 0 2px;
}

.detail-price .price-unit {
  font-size: 14px;
  color: #909399;
}

.detail-info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.info-item {
  display: flex;
  font-size: 14px;
}

.info-label {
  color: #909399;
  min-width: 80px;
}

.info-value {
  color: #303133;
  flex: 1;
}

.detail-address {
  margin-bottom: 16px;
  font-size: 14px;
}

.detail-description {
  margin-top: 16px;
}

.detail-description .info-label {
  margin-bottom: 8px;
  display: block;
}

.desc-content {
  color: #606266;
  line-height: 1.8;
  font-size: 14px;
  background: #f5f7fa;
  padding: 12px;
  border-radius: 4px;
}

@media screen and (max-width: 1400px) {
  .house-card {
    width: calc(33.33% - 14px);
  }
}

@media screen and (max-width: 1100px) {
  .house-card {
    width: calc(50% - 10px);
  }
}

@media screen and (max-width: 700px) {
  .house-card {
    width: 100%;
  }
}
</style>
