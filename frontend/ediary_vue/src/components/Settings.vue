<template>
<div class="baseBacking">
    <div class="bg-white rounded-xl p-2 w-1/6 h-1/6 shadow-xl relative flex flex-col justify-evenly content-center items-center">
        <h2 class="text-xl font-semibold mb-4">Настройки</h2>
        <button @click="logout" class="baseBtn w-1/2">
            Выйти
        </button>
        <button @click="$emit('clicked')" class="absolute top-2 right-2 text-gray-500 hover:text-black">
            ✕
        </button>
    </div>
</div>
</template>

<script setup lang="ts">
import router from '@/router';
import { useAuthStore } from '@/stores/auth';
const emit = defineEmits(["clicked"])
const auth = useAuthStore()
function logout(): void {
    auth.logout();
    router.push('/login')
    emit('clicked');
}
</script>
