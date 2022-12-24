<template>
    <el-container>
        <el-aside>
            aside
        </el-aside>
        <el-main style="padding: 0;">
            <chat-panel />
        </el-main>
    </el-container>
</template>

<script>
import ChatPanel from "@/components/chat-panel";
import util from '@/api/util'
export default {
    components: {
        // "message-group": MessageGroup
        "chat-panel": ChatPanel,
        // "message-pabel": MessagePabel
    },
    data() {
        return {
            message: [{
                me: 0,
                friend: 1,
                username: "hahaha",
                text: "hahaha",
                time: "2020"
            }]
        }
    },
    methods: {
        switchGroup(index, id) {
            this.nowSwitchId = id
            this.nowSwitch = index

            // 隐藏小红点
            if (this.concats[index].message.isNewMessage !== undefined) {
                this.concats[index].message.isNewMessage = false
                this.concats[index].message.newMessageCount = 0
            }
        },
    },
    computed: {
        getUserAvatarSrc() {
            return function (id) {
                const src = util.getUserAvatarSrc(id)
                return src
            }
        }
    }
}
</script>

<style lang="scss">

</style>