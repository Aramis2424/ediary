<template>

<div class="w-[90vw] h-full flex items-center opacity-95">
    <v-md-editor v-model="localContent" height="90vh" class="w-full"/>
</div>
  
</template>

<script setup lang="ts">
import { ref, watch, type Ref } from 'vue';

import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import '@kangc/v-md-editor/lib/theme/style/github.css';
import ruRU from '@kangc/v-md-editor/lib/lang/ru-RU';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import Prism from 'prismjs';

VMdEditor.use(githubTheme, { Prism });
VMdEditor.lang.use('ru-RU', ruRU)

const props = defineProps<{
    content: string
}>()

const emit = defineEmits<{
  (event: 'update:content', value: string): void
}>()

const localContent: Ref<string> = ref(props.content)

watch(
  () => props.content,
  (newVal) => {
    localContent.value = newVal
  },
  { immediate: true }
)

watch(localContent, (val) => {
  emit('update:content', val)
})

</script>
