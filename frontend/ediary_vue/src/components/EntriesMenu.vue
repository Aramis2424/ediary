<script setup lang="ts">
import EntryCard from './EntryCard.vue';
</script>

<template>
<!-- <div class="h-screen flex justify-center items-center">
    <EntriesMenu />
</div> -->
  <div class="h-[90vh] w-full max-w-5xl flex flex-wrap gap-5 justify-center content-start p-4 overflow-y-scroll hide-scrollbar">
    <EntryCard title="День трех встречь" date="2020-01-05" :mood="7" :work="9"/>
    <EntryCard title="Много разговоров" date="2020-01-06" :mood="5" :work="8"/>
    <EntryCard title="Отдых" date="2020-01-07" :mood="10" :work="4"/>
    <EntryCard title="Начал за здравие ..." date="2020-01-08" :mood="3" :work="10"/>
    <EntryCard title="17 часов ..." date="2020-01-09" :mood="9" :work="2"/>
    <EntryCard title="Работал за двоих" date="2020-01-10" :mood="1" :work="10"/>
  </div>
</template>

<style scoped>
</style>
