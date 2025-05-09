export interface Monitor {
    id: string;
    name: string;
    serverAddress: string;
    scheduleInterval: number;
    monitorStatus: 'HEALTHY' | 'UNKNOWN' | 'CRITICAL';
    type: string;
    status: boolean;
    notificationStatus: boolean,
    createdAt: string;
    lastNotifiedAt: string;
    port?: number;
    isHttps?: boolean;
    // type: 'PING_CHECK' | 'HTTP_CHECK';
}


export interface MonitorFormData {
    name: string;
    address: string;
    type: 'PORT_CHECK' | 'HTTP_CHECK';
    port: string;
    protocol?: 'HTTP' | 'HTTPS';
    scheduleInterval: number; // New field for schedule interval
}

export interface FormErrors {
    name?: string;
    address?: string;
    port?: string;
    [key: string]: string | undefined;
}

export interface MonitorResult {
    monitorId: string,
    userId: number,
    serverAddress: number,
    status: "HEALTHY" | "UNKNOWN" | "CRITICAL"
}