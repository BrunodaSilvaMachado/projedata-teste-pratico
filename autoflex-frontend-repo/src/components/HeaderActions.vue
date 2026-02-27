<template>
  <div class="header-actions">
    <slot />
  </div>
</template>

<style>
.header-actions {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 1rem;
}

/* ensure buttons passed via slot also receive styles */
.header-actions ::v-deep button,
.header-actions button {
  background: #3b82f6;
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 8px;
  cursor: pointer;
}
</style>
