<template>
    <div style="padding:10%;" v-if="this.user">
        <el-row>
            <el-col :span="5">
                <el-image style="width: 80px; height: 80px" :src="this.src" fit="fill"></el-image>
            </el-col>
            <el-col :span="12" :push="3" style="text-align:left;">
                <!-- username -->
                <el-row style="font-size: 40px;">
                    {{ this.user.username }}
                </el-row>
                <!-- id -->
                <el-row style="margin-top: 10px; font-size: 20px;">
                    {{ this.user.id }}
                </el-row>
            </el-col>
        </el-row>
        <el-descriptions style="margin: 10px 20px 0px 20px;" :column="2">
            <el-descriptions-item label="性别">
                {{ this.user.sex }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">
                {{ this.user.birth }}</el-descriptions-item>
            <el-descriptions-item label="邮箱" :span="2">
                {{ this.user.email }}</el-descriptions-item>
            <el-descriptions-item label="个性签名" :span="2">
                {{ this.user.info }}</el-descriptions-item>
        </el-descriptions>
        <el-row style="margin-top: 40%;">
            <el-col :span="10">
                <el-button @click="delFriend()">
                    删除好友
                </el-button>
            </el-col>
            <el-col :span="10">
                <el-button @click="startChat()">
                    发起聊天
                </el-button>
            </el-col>
        </el-row>


    </div>
</template>
    
<script>
import util from '@/api/util';
export default {
    props: {
        user: {
            type: Object,
            required: true
        },
        delFriend: {
            type: Function
        },
        startChat: {
            type: Function
        }
    },
    data() {
        return {

        }
    },
    computed: {
        src: function () {
            return util.getUserAvatarSrc(this.user.id)
        }
    }
}
</script>
    
<style>
.label {
    background-color: #efefef;
}
</style>