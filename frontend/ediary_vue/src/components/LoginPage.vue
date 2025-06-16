<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 bg-lemon">
    <div class="w-full max-w-md bg-white p-8 rounded-2xl text-[#5A3A2B] font-semibold shadow opacity-95">
      <h2 class="text-3xl text-center mb-3"> Вход </h2>
      <form @submit.prevent="handleLogin" class="space-y-8">
        <div class="space-y-3">
            <div class="space-y-4">
                <p class="text-xl text-[#5A3A2B] -mb-3"> Логин </p>
                <input
                v-model="login"
                type="text"
                placeholder="Введите Ваш логин"
                class="w-full p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
                />
            </div>
            <div class="space-y-4">
                <p class="text-xl text-[#5A3A2B] -mb-3"> Пароль </p>
                <input
                v-model="password"
                type="password"
                placeholder="Введите Ваш пароль"
                class="w-full p-3 rounded-2xl border-[#F4A261] border-2 focus:outline-none focus:ring-1 focus:ring-[#e09455]"
                />
            </div>
        </div>
        <button
          type="submit"
          class="baseBtn p-3 w-full text-xl"
        >
          Войти
        </button>
      </form>
      <p class="text-base text-center">
        Нет аккаунта?
        <router-link to="/register" class="text-[#E67E22] font-bold hover:underline"> Регистрация </router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();

const login = ref('');
const password = ref('');

function handleLogin() {
  console.log('Регистрация:', login.value, password.value);
  router.push('/home');
  // TODO: логика регистрации
}
</script>
