import {createRouter, createWebHashHistory, createWebHistory} from 'vue-router'
import Layout from "../layout/Layout";

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect: '/index',
    children: [
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/User')
      },
      {
        path: 'person',
        name: 'Person',
        component: () => import('../views/Person')
      },
      {
        path: 'device',
        name: 'Device',
        component: () => import('../views/Device')
      },
      {
        path: 'places',
        name: 'Places',
        component: () => import('../views/Places')
      },
      {
        path: 'supplier',
        name: 'Supplier',
        component: () => import('../views/Supplier')
      },
      {
        path: 'order',
        name: 'Order',
        component: () => import('../views/Order')
      },
      {
        path: 'repair',
        name: 'Repair',
        component: () => import('../views/Repair')
      },
      {
        path: 'storage',
        name: 'Storage',
        component: () => import('../views/Storage')
      },
      {
        path: 'allocate',
        name: 'Allocate',
        component: () => import('../views/Allocate')
      },
      {
        path: 'updateRecorder',
        name: 'UpdateRecorder',
        component: () => import('../views/UpdateRecorder')
      },
      {
        path: 'index',
        name: 'Index',
        component: () => import('../views/Index')
      },
      {
        path: 'notice',
        name: 'Notice',
        component: () => import('../views/Notice')
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login')
  }
]

const router = createRouter({
  // history: createWebHistory(process.env.BASE_URL),
  history: createWebHashHistory(),
  routes
})

export default router
