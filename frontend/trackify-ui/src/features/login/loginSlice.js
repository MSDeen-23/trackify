import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  login: false,
  user: {},
};

export const loginSlice = createSlice({
  name: "login",
  initialState,
  reducers: {
    changeLoginState: (state, action) => {
      state.login = action;
    },
    addUser: (state, action) => {
      state.user = action.payload;
    },
    deleteUser: (state, action) => {
      state.user = {};
    },
  },
});

export const { changeLoginState, addUser, deleteUser } = loginSlice.actions;
export default loginSlice.reducer;
