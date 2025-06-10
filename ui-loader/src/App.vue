<script setup>
import { useCheck } from "./hook.js";

const { text } = useCheck();
</script>

<template>
  <div class="ui-loader">
    <v-progress-circular
      color="primary"
      indeterminate
      :size="120"
      :width="16"
    />

    <div class="text-box">
      <div class="text">{{ text }}</div>
    </div>
  </div>
</template>

<style scoped>
.ui-loader {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: 100vw;
  height: 100vh;

  position: relative;
  top: 0;
  left: 0;

  display: flex;
  align-items: center;
  justify-content: center;

  .text-box {
    position: absolute;
    bottom: 20%;
    left: 0;
    right: 0;

    display: flex;
    align-items: center;
    justify-content: center;

    padding: 1em;
    font-size: 24px;

    .text {
      word-wrap: break-word;
      word-break: break-all;
    }
  }
}
</style>
