<script setup lang="ts">
import FieldLabel from "./FieldLabel.vue";

const props = defineProps({
  fileName: {
    type: String,
    required: true,
  },
  fileError: {
    type: String,
    required: true,
  },
  info: {
    type: String,
    default: "Max file size: 10MB",
  },
});

const emit = defineEmits(["change", "remove"]);
</script>
 
<template>
  <FieldLabel
    label="File"
    :required="false"
  />
  <div
    v-if="info !== ''"
    class="rounded-md text-sm text-blue-500"
  >
    {{ info }}
  </div>
  <input
    id="file"
    type="file"
    class="hidden"
    @change="$emit('change', $event)"
  >

  <div class="flex w-full rounded-md border border-slate-500/10 bg-slate-500/5 p-2 px-3 text-slate-500">
    <div
      v-if="fileName"
      class="flex w-full items-center justify-between gap-2"
    >
      <div
        class="truncate text-sm font-medium"
      >
        {{ fileName }}
      </div>
      <div class="flex gap-1">
        <label
          for="file"
          class="block cursor-pointer rounded-md p-1 text-sm font-medium text-slate-400 hover:bg-blue-100 hover:text-blue-500"
        >
          <span class="material-symbols-outlined m-auto block">
            edit
          </span>
        </label>
        <button
          type="button"
          class="rounded-md p-1 text-sm font-medium text-slate-400 hover:bg-red-100 hover:text-red-500"
          @click="$emit('remove')"
        >
          <span class="material-symbols-outlined m-auto block">
            delete
          </span>
        </button>
        <!-- edit -->
      </div>
    </div>

    <label
      v-else
      for="file"
      class="flex w-full cursor-pointer items-center justify-between gap-2 text-sm font-medium"
    >Choose a file
      <span class="material-symbols-outlined">
        attach_file
      </span>
    </label>
  </div>

  <div
    v-if="fileError"
    class="mx-1 rounded-md bg-red-50 py-1 px-2 text-sm text-red-500"
  >
    <span
      v-for="error in fileError"
      :key="error"
    >{{ error }}</span>
  </div>
</template>
 
<style scoped>

</style>