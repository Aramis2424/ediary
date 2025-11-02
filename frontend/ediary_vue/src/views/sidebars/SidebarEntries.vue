<template>
  <div class="sidebar-inner">
    <button class="icon-btn" title="Назад" @click="ui.gotoHome(router)">
      <ArrowLeft />
    </button>

    <button class="icon-btn" title="Поиск записей" @click="onSearch">
    <Search />
    </button>
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft, Search } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { useUiStore } from '@/stores/ui'

const router = useRouter()
const ui = useUiStore()

function onSearch() {
  ui.showSearching = !ui.showSearching
}

</script>

<style scoped>
</style>
