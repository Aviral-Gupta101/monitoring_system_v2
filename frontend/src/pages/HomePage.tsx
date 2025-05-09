import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { toast } from 'sonner';
import { Button } from "../components/ui/button";
import Header from '../components/Header';
import { Monitor, MonitorFormData, MonitorResult } from '../types';
import MonitorList from '@/components/MonitorList';
import CreateMonitorDialog from '@/components/CreateMonitorDialog';
import { SERVER_ADDRESS, SOCKET_ADDRESS } from '@/lib/global';
import { io, Socket } from "socket.io-client";

function getTimestampForComparison(): string {
  const now = new Date();

  const pad = (num: number, size: number) => num.toString().padStart(size, '0');

  const year = now.getFullYear();
  const month = pad(now.getMonth() + 1, 2);
  const day = pad(now.getDate(), 2);

  const hours = pad(now.getHours(), 2);
  const minutes = pad(now.getMinutes(), 2);
  const seconds = pad(now.getSeconds(), 2);

  const microseconds = pad(now.getMilliseconds() * 1000, 6);

  return `${year}-${month}-${day}T${hours}:${minutes}:${seconds}.${microseconds}`;
}


interface HomePageProps {
  onLogout: () => void;
}

const HomePage: React.FC<HomePageProps> = ({ onLogout }) => {
  const [monitors, setMonitors] = useState<Monitor[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [isDialogOpen, setIsDialogOpen] = useState<boolean>(false);
  const [socket, setSocket] = useState<Socket | null>(null);

  // http://localhost:8080/user/monitor/delete/12
function onDelete(id : string) : void {

  axios.delete(SERVER_ADDRESS + "user/monitor/delete/" + id, {
    headers: { Authorization: `Bearer ${localStorage.getItem("authToken")}` }
  })
  .then((_) => {

    toast.success("Monitor deleted");

    setMonitors((monitors) => {
      return monitors.filter(item => item.id != id);
    });

  })
  .catch((e : any) => {
    const errorMsg = e?.response?.data?.message || "Please try again !!";
    toast.error("Unable to delete monitor", { description: errorMsg,});
  });
}

  const fetchMonitors = async (): Promise<void> => {
    setIsLoading(true);
    try {
      const token = localStorage.getItem('authToken');
      const response: any = await axios.get(SERVER_ADDRESS + 'user/monitors', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setMonitors(response.data);
    } catch (error) {
      console.error('Error fetching monitors:', error);
      toast.error('Failed to load monitors', {
        description: 'Please try again later',
      });
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {

    fetchMonitors();

    const token = localStorage.getItem('authToken');
    if (!token) {
      console.error('No token found');
      return;
    }

    const newSocket = io(SOCKET_ADDRESS, {
      path: "/socket.io/",
      transports: ["websocket"],
      query: { token },
      reconnectionAttempts: 3,
      reconnectionDelay: 1000,
    });

    newSocket.on('connect', () => {
      console.log('✅ Connected to socket server!');
    });

    newSocket.on('message', (data: MonitorResult) => {
      console.log('📩 Message received:', data);
    });

    newSocket.on('connect_error', (error) => {
      console.error('❌ Socket connection error:', error.message);
    });

    newSocket.on('disconnect', (reason) => {
      console.log('⚡ Disconnected from socket:', reason);
    });

    newSocket.on("status", ( data : MonitorResult) => {
      
      setMonitors((monitorList) => {
        
        return monitorList.map(item => {

          if(item.id != data.monitorId)
              return item;

          var newdata = item;
          newdata.monitorStatus = data.status;
          newdata.lastNotifiedAt =  getTimestampForComparison();
          return newdata;
        });
      });
    });

    setSocket(newSocket);

    return () => {
      newSocket.disconnect();
    };
  }, []);

  const handleCreateMonitor = async (monitorData: MonitorFormData): Promise<void> => {
    try {
      const token = localStorage.getItem('authToken');
      const response: any = await axios.post(SERVER_ADDRESS + "user/monitor/create",
        {
          name: monitorData.name,
          serverAddress: monitorData.address,
          scheduleInterval: monitorData.scheduleInterval > 0 ? parseInt(monitorData.scheduleInterval.toString()) : 30,
          type: monitorData.type,
          ...(monitorData.type === 'PORT_CHECK' && {
            port: monitorData.port ? parseInt(monitorData.port) : undefined,
          }),
          ...(monitorData.type === 'HTTP_CHECK' && {
            https: monitorData.protocol === 'HTTPS',
          }),
        },
        {
          headers: { Authorization: `Bearer ${token}` }
        }
      );

      toast.success('Monitor created', {
        description: `${monitorData.name} has been created successfully`,
      });

      setMonitors(prev => [...prev, response.data]);
      setIsDialogOpen(false);
    } catch (error: any) {
      console.error('Error creating monitor:', error);
      toast.error('Failed to create monitor', {
        description: error?.response?.data?.message || "An error occurred",
      });
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <Header onLogout={onLogout} />
      <main className="container mx-auto px-4 py-6">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">Service Monitors</h1>
          <Button onClick={() => setIsDialogOpen(true)}>
            Create New Monitor
          </Button>
        </div>
        <MonitorList onDelete={onDelete} monitors={monitors} isLoading={isLoading} />
        <CreateMonitorDialog 
          open={isDialogOpen} 
          onOpenChange={setIsDialogOpen}
          onCreate={handleCreateMonitor}
        />
      </main>
    </div>
  );
};

export default HomePage;
