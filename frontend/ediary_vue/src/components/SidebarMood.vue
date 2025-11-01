<template>
  <div class="sidebar-inner">
    <button class="icon-btn" title="Назад" @click="ui.gotoHome(router)">
      <ArrowLeft />
    </button>

    <!-- <button class="icon-btn" title="Отметить настроение" @click="onTest">
      <Smile />
    </button> -->
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft, Smile } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { useUiStore } from '@/stores/ui'

const router = useRouter()
const ui = useUiStore()
function onTest() { ui.showSurveyMood = !ui.showSurveyMood }
</script>

<style scoped>
</style>
