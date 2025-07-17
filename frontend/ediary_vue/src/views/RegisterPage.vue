<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100 bg-lemon">
    <div class="w-full max-w-md bg-white p-2 mt-1 mb-2 rounded-2xl text-[#5A3A2B] font-semibold shadow opacity-95">
      <h2 class="text-3xl text-center mb-3"> Регистрация </h2>
      <form @submit.prevent="handleRegister" class="space-y-6">
        <div class="space-y-3">
            <FormField v-model="name" type="text" placeholder="Введите Ваше имя" label="Имя" />
            <FormField v-model="login" type="text" placeholder="Введите Ваш логин" label="Логин" />
            <FormField v-model="password" type="password" placeholder="Введите Ваш пароль" label="Пароль" />
            <FormField v-model="confirmPassword" type="password" placeholder="Повторите Ваш пароль" label="Повтрите паоль" />
            <FormField v-model="birthdate" type="date" placeholder="Укажите Вашу дату рождения" label="Дата рождения" />
        </div>
        <button
          type="submit"
          class="baseBtn p-3 w-full h-[7vh] max-h-14 text-xl text-center flex items-center justify-center"
        >
          Зарегистрироваться
        </button>
      </form>
      <p class="text-base text-center">
        Уже зарегисрированы?
        <router-link to="/login" class="text-[#E67E22] font-bold hover:underline"> Войти </router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import type { OwnerCreateDTO } from '@/types/Owner';
import { useAuthStore } from '@/stores/auth';
import FormField from '@/components/FormField.vue';

const router = useRouter();
const auth = useAuthStore();

const name = ref('');
const login = ref('');
const birthdate = ref('')
const password = ref('');
const confirmPassword = ref('');

async function handleRegister() {
  if (password.value !== confirmPassword.value) {
    alert('Пароли не совпадают!');
    return;
  }

  const newOwner: OwnerCreateDTO = {
    name: name.value,
    birthDate: birthdate.value,
    login: login.value,
    password: password.value,
  }
  try {
    await auth.register(newOwner)
    router.push('/home');
  } catch {
    alert('Такой логин уже существует')
  }
}
</script>
