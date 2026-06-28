<template>
  <div class="profile-container">
    <el-card shadow="never">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="基本信息" name="basic">
          <div class="basic-info">
            <div class="avatar-section">
              <el-avatar :size="120" :src="profileForm.avatar" icon="el-icon-user-solid"></el-avatar>
              <el-upload
                class="avatar-uploader"
                :action="uploadUrl"
                :headers="uploadHeaders"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                accept="image/*"
              >
                <el-button size="small" type="primary" icon="el-icon-upload">更换头像</el-button>
              </el-upload>
            </div>
            <el-form ref="profileForm" :model="profileForm" :rules="basicRules" label-width="100px" class="profile-form">
              <el-form-item label="用户名">
                <el-input v-model="profileForm.username" disabled></el-input>
              </el-form-item>
              <el-form-item label="昵称" prop="nickname">
                <el-input v-model="profileForm.nickname" placeholder="请输入昵称"></el-input>
              </el-form-item>
              <el-form-item label="邮箱" prop="email">
                <el-input v-model="profileForm.email" placeholder="请输入邮箱"></el-input>
              </el-form-item>
              <el-form-item label="手机号" prop="phone">
                <el-input v-model="profileForm.phone" placeholder="请输入手机号"></el-input>
              </el-form-item>
              <el-form-item label="性别">
                <el-radio-group v-model="profileForm.gender">
                  <el-radio :label="1">男</el-radio>
                  <el-radio :label="2">女</el-radio>
                  <el-radio :label="0">未知</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-form-item label="个人简介">
                <el-input
                  v-model="profileForm.bio"
                  type="textarea"
                  :rows="4"
                  placeholder="请输入个人简介"
                ></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" :loading="submitLoading" @click="handleUpdateProfile">保存修改</el-button>
                <el-button @click="loadProfile">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-tab-pane>

        <el-tab-pane label="修改密码" name="password">
          <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="100px" class="password-form">
            <el-form-item label="旧密码" prop="oldPassword">
              <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入旧密码" show-password></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码" show-password></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="passwordLoading" @click="handleUpdatePassword">修改密码</el-button>
              <el-button @click="handleResetPassword">重置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import { getProfile, updateProfile, updatePassword } from '@/api/profile'
import { getToken } from '@/utils/auth'

export default {
  name: 'Profile',
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入新密码'))
      } else if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      activeTab: 'basic',
      submitLoading: false,
      passwordLoading: false,
      uploadUrl: '/api/profile/avatar',
      profileForm: {
        id: null,
        username: '',
        nickname: '',
        email: '',
        phone: '',
        avatar: '',
        gender: 0,
        bio: ''
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      basicRules: {
        nickname: [
          { required: true, message: '请输入昵称', trigger: 'blur' },
          { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
        ]
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    uploadHeaders() {
      return {
        'Authorization': 'Bearer ' + getToken()
      }
    }
  },
  mounted() {
    this.loadProfile()
  },
  methods: {
    async loadProfile() {
      try {
        const res = await getProfile()
        this.profileForm = { ...res.data }
        this.$store.commit('user/SET_USER_INFO', res.data)
      } catch (error) {
        console.error('获取个人信息失败:', error)
        const userInfo = this.$store.getters.userInfo
        if (userInfo) {
          this.profileForm = { ...this.profileForm, ...userInfo }
        }
      }
    },
    async handleUpdateProfile() {
      try {
        const valid = await this.$refs.profileForm.validate()
        if (valid) {
          this.submitLoading = true
          await updateProfile(this.profileForm)
          this.$message.success('修改成功')
          this.$store.dispatch('user/getUserInfo')
        }
      } catch (error) {
        if (error !== false) {
          console.error('修改失败:', error)
        }
      } finally {
        this.submitLoading = false
      }
    },
    async handleUpdatePassword() {
      try {
        const valid = await this.$refs.passwordForm.validate()
        if (valid) {
          this.passwordLoading = true
          await updatePassword(this.passwordForm)
          this.$message.success('密码修改成功')
          this.handleResetPassword()
        }
      } catch (error) {
        if (error !== false) {
          console.error('密码修改失败:', error)
        }
      } finally {
        this.passwordLoading = false
      }
    },
    handleResetPassword() {
      this.passwordForm = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      }
      this.$refs.passwordForm && this.$refs.passwordForm.resetFields()
    },
    beforeAvatarUpload(file) {
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
    handleAvatarSuccess(response) {
      if (response.code === 200) {
        this.profileForm.avatar = response.data
        this.$message.success('头像上传成功')
        this.$store.dispatch('user/getUserInfo')
      } else {
        this.$message.error(response.message || '上传失败')
      }
    }
  }
}
</script>

<style scoped>
.profile-container {
  padding: 0;
  max-width: 900px;
  margin: 0 auto;
}

.basic-info {
  display: flex;
  flex-direction: row;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 60px;
  padding-top: 20px;
}

.avatar-uploader {
  margin-top: 20px;
}

.profile-form {
  flex: 1;
}

.password-form {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px 0;
}

@media (max-width: 768px) {
  .basic-info {
    flex-direction: column;
  }
  .avatar-section {
    margin-right: 0;
    margin-bottom: 30px;
  }
}
</style>
