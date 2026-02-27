<template>
  <div class="table-container">
    <table>
      <thead>
        <tr>
          <slot name="header" />
        </tr>
      </thead>
      <tbody>
        <slot name="body" />
      </tbody>
    </table>
  </div>
</template>

<style>
.table-container {
  width: 100%;
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.table-container table {
  width: 100%;
  border-collapse: collapse;
}

/* deep selectors only for slot content (th, td, buttons inside cells) */
.table-container ::v-deep th,
.table-container ::v-deep td {
  padding: 0.8rem;
}

.table-container ::v-deep th {
  border-bottom: 2px solid #e5e7eb;
}

.table-container ::v-deep button.edit {
  background: #facc15;
  border: none;
  padding: 0.4rem 0.6rem;
  border-radius: 6px;
  cursor: pointer;
}

.table-container ::v-deep button.delete {
  background: #ef4444;
  color: white;
  border: none;
  padding: 0.4rem 0.6rem;
  border-radius: 6px;
  margin-left: 0.5rem;
  cursor: pointer;
}
</style>
