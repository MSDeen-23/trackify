import axios from "axios";
import Cookies from "js-cookie";
// this links doesn't need any authentication
const linksWithoutAuthentication = [
  "/api/v1/user/auth/register-admin",
  "/api/v1/user/auth/login",
];

// initialize axios instance
const instance = axios.create({
  baseURL: `http://localhost:8381/api/v1`,
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
  },
});

// wrapper for axios get
export const get = async (url, params = {}) => {
  try {
    let response;
    if (linksWithoutAuthentication.includes(url)) {
      response = await instance.get(url, { params });
    } else {
      const authToken = Cookies.get("authToken");

      response = await instance.get(url, {
        headers: { Authorization: `Bearer ${authToken}` },
      });
    }
    return response.data;
  } catch (error) {
    // Handle errors here (e.g., log or throw custom errors)
    throw error;
  }
};

export const post = async (url, data = {}) => {
  try {
    const response = await instance.post(url, data);
    return response.data;
  } catch (error) {
    throw error;
  }
};
