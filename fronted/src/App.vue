<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'app',
}
</script>

<style lang="scss">
* {
  margin: 0;
  padding: 0;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}

ul,ol {
  list-style: none;
}

#app {
  height: 100vh;
  width: 100vw;
  background-image: linear-gradient(45deg, #409eff, #bbddff);
}

.el-notification__content {
  margin-top: 20px;
  p {
    display: flex;
  }
  .notify-image {
    margin-right: 10px;
    width: 50px;
    height: 50px;
  }
  .notify-content {
    .notify-title {
      display: block;
      margin-bottom: 2px;
    }
  }
}

::-webkit-scrollbar {
  width: 8px;
  padding-right: 4px;
  background-color:#f8f8f8;
}
::-webkit-scrollbar-thumb {
  -webkit-border-radius: 4px;
  border-radius: 4px;
  background-color: #c6c6c6;
}
::-webkit-scrollbar-thumb:window-inactive {
  background-color: #f8f8f8;
}


</style>
