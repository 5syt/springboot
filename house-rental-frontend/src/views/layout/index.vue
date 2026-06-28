<template>
  <el-container class="layout-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="layout-aside">
      <div class="logo">
        <span v-if="!isCollapse">房屋租赁系统</span>
        <span v-else>HR</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        router
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        unique-opened
      >
        <template v-for="route in menuRoutes">
          <template v-if="route.children && route.children.length > 0">
            <el-submenu :index="resolvePath(route.path)" :key="route.path">
              <template slot="title">
                <i :class="(route.meta && route.meta.icon) || 'el-icon-menu'"></i>
                <span slot="title">{{ route.meta && route.meta.title }}</span>
              </template>
              <el-menu-item
                v-for="child in route.children"
                :key="child.path"
                :index="resolvePath(route.path + '/' + child.path)"
              >
                <i :class="(child.meta && child.meta.icon) || 'el-icon-document'"></i>
                <span slot="title">{{ child.meta && child.meta.title }}</span>
              </el-menu-item>
            </el-submenu>
          </template>
          <template v-else>
            <el-menu-item :index="resolvePath(route.path)" :key="route.path">
              <i :class="(route.meta && route.meta.icon) || 'el-icon-menu'"></i>
              <span slot="title">{{ route.meta && route.meta.title }}</span>
            </el-menu-item>
          </template>
        </template>
      </el-menu>
    </el-aside>

    <el-container class="main-container">
      <el-header class="layout-header">
        <div class="header-left">
          <i class="el-icon-s-fold toggle-btn" @click="toggleSideBar" :class="{'el-icon-s-unfold': isCollapse}"></i>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index">
              {{ (item.meta && item.meta.title) || item.name }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="header-right">
          <el-dropdown trigger="click" @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="userInfo.avatar" icon="el-icon-user-solid"></el-avatar>
              <span class="username">{{ userInfo.nickname || userInfo.username || '管理员' }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">
                <i class="el-icon-user"></i> 个人中心
              </el-dropdown-item>
              <el-dropdown-item command="password">
                <i class="el-icon-key"></i> 修改密码
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
                <i class="el-icon-switch-button"></i> 退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="layout-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { asyncRoutes, constantRoutes } from '@/router'

export default {
  name: 'Layout',
  computed: {
    isCollapse() {
      return this.$store.getters.sidebarCollapsed
    },
    userInfo() {
      return this.$store.getters.userInfo || {}
    },
    activeMenu() {
      return this.$route.path
    },
    menuRoutes() {
      const routes = JSON.parse(JSON.stringify([...constantRoutes, ...asyncRoutes]))
      const userRoles = this.$store.getters.roles || []
      const hasRole = (route) => {
        if (!route.meta || !route.meta.roles) return true
        return route.meta.roles.some(role => userRoles.includes(role))
      }
      return routes.filter(route => {
        if (route.hidden) return false
        if (!hasRole(route)) return false
        if (route.children) {
          route.children = route.children.filter(child => !child.hidden && hasRole(child))
        }
        return route.component && route.children && route.children.length > 0 && route.path !== '/' && route.path !== '/login' && route.path !== '/404'
      }).map(route => {
        if (route.path === '/') {
          return route.children[0]
        }
        return route
      })
    },
    breadcrumbs() {
      return this.$route.matched.filter(item => item.meta && item.meta.title)
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    resolvePath(path) {
      if (path.startsWith('/')) return path
      return '/' + path
    },
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/profile')
          break
        case 'password':
          this.$router.push('/profile')
          break
        case 'logout':
          this.handleLogout()
          break
      }
    },
    async handleLogout() {
      try {
        await this.$confirm('确定要退出登录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await this.$store.dispatch('user/logout')
        this.$message.success('退出成功')
        this.$router.push('/login')
      } catch (e) {
        if (e !== 'cancel') {
          console.error(e)
        }
      }
    }
  }
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.layout-aside {
  background-color: #304156;
  transition: width 0.3s;
  overflow: hidden;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  background-color: #2b2f3a;
  white-space: nowrap;
  overflow: hidden;
}

.layout-aside >>> .el-menu {
  border-right: none;
}

.main-container {
  overflow: hidden;
}

.layout-header {
  background-color: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
}

.toggle-btn {
  font-size: 20px;
  cursor: pointer;
  margin-right: 20px;
  color: #606266;
  transition: color 0.3s;
}

.toggle-btn:hover {
  color: #409EFF;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #606266;
}

.user-info .username {
  margin: 0 8px;
  font-size: 14px;
}

.layout-main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}

.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.3s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
</style>
