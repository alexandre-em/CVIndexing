import axios, { AxiosResponse } from 'axios';

const instance = axios.create({ baseURL: 'http://localhost/api/v1/' })

export const search = (keywords:string): Promise<AxiosResponse<CVModel[]>> => {
	return instance.get(`cv?keyword=${keywords}`);
}

export const getCVDetails = (id: string): Promise<AxiosResponse<CVResponse>> => {
	return instance.get(`cv/${id}`);
}

export const uploadCV = (file: File): Promise<AxiosResponse<CVModel>> => {
	const formData: FormData = new FormData();
	formData.append('file', file);
	const config = { headers: { 'content-type': 'multipart/form-data' } }
	return instance.post(`cv`, formData, config);
}
