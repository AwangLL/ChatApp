import Vue from "vue";
import VueRouter from "vue-router";
import UserLogin from "@/views/user-login"
import UserReg from "@/views/user-reg"
import UserHome from "@/views/user-home"

Vue.use(VueRouter)

const router = new VueRouter({
    routes: [
        {
            path: '/',
            redirect: 'login'
        },
        {
            path: '/login',
            name: 'login',
            component: UserLogin
        },
        {
            path: '/reg',
            name: 'reg',
            component: UserReg
        },
        {
            path: '/home',
            name: 'home',
            component: UserHome
        }
    ]
})

export default router