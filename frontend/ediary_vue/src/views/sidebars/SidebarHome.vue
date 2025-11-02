<template>
  <div class="sidebar-inner">
    <button class="icon-btn" title="Настройки" @click="onSettings">
      <Settings />
    </button>

    <button class="icon-btn" title="О себе" @click="onAbout">
      <User />
    </button>
  </div>
</template>

<script setup lang="ts">
import { Settings, User } from 'lucide-vue-next'
import { useUiStore } from '@/stores/ui'

const ui = useUiStore()
function onSettings() { ui.showSettings = !ui.showSettings }
function onAbout() { ui.showAboutYourself = !ui.showAboutYourself }
</script>

<style scoped>
</style>
