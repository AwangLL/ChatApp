<template>
<div>
    <el-col :span="4">
        <el-image style="width: 45px; height: 45px" fit="fill" :src="this.src"></el-image>
    </el-col>
        
    <el-col :span="6">
        {{this.user.username}}
    </el-col>
</div>
</template>

<script>
import util from '@/api/util'
export default {
    props: {
        user: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            src: util.getUserAvatarSrc(this.user.id)
        }
    }
}
</script>