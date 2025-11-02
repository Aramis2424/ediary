<template>
  <div class="sidebar-inner">
    <button class="icon-btn" title="Назад" @click="ui.gotoMenu(router)">
      <ArrowLeft />
    </button>

    <!-- <button class="icon-btn" title="Сохранить" @click="onSave">
      <Save />
    </button>

    <button class="icon-btn danger" title="Удалить" @click="onDelete">
      <Trash2 />
    </button> -->
  </div>
</template>

<script setup lang="ts">
import { ArrowLeft, Save, Trash2 } from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import { defineEmits } from 'vue'
import { useUiStore } from '@/stores/ui'

const router = useRouter()
const ui = useUiStore()
const emit = defineEmits<{
  (e: 'save'): void
  (e: 'delete'): void
}>()

function onSave() { emit('save') }
function onDelete() { emit('delete') }
</script>

<style scoped>
.icon-btn.danger { color:#ff816b; }
</style>
