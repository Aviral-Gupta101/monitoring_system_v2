import React, { useState } from 'react';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "./ui/table";
import { Badge } from "./ui/badge";
import { Skeleton } from "./ui/skeleton";
import { Monitor } from '../types';
import { MoreVertical, Check, X, Trash2 } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from "./ui/dropdown-menu";
import { Button } from "./ui/button";

interface MonitorListProps {
  monitors: Monitor[];
  isLoading: boolean;
  onDisable?: (id: string) => void;
  onEnable?: (id: string) => void;
  onDelete?: (id: string) => void;
}

const statusVariants: Record<string, string> = {
  HEALTHY: "bg-green-500",
  UNKNOWN: "bg-yellow-500",
  CRITICAL: "bg-red-500"
};

function formatTimeDifference(pastTimeString: string): string {
  if(pastTimeString == null || pastTimeString == "" )
    return "Never Updated";
  
  const pastTime = new Date(pastTimeString);
  const currentTime = new Date();
  
  const diffMs = currentTime.getTime() - pastTime.getTime(); // difference in milliseconds
  const diffMinutes = Math.floor(diffMs / 60000); // 1 minute = 60000 ms
  
  if (diffMinutes <= 0) {
    return "Just now";
  } else if (diffMinutes < 60) {
    return `${diffMinutes}m ago`;
  } else {
    const hours = Math.floor(diffMinutes / 60);
    const minutes = diffMinutes % 60;
    return `${hours}h ${minutes}m ago`;
  }
}

const MonitorList: React.FC<MonitorListProps> = ({ 
  monitors, 
  isLoading,
  onDisable = () => {},
  onEnable = () => {},
  onDelete = () => {} 
}) => {
  
  if (isLoading) {
    return (
      <div className="bg-white rounded-md shadow">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>S/N</TableHead>
              <TableHead>Monitor Name</TableHead>
              <TableHead>Address</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Type</TableHead>
              <TableHead>Last Updated</TableHead>
              <TableHead></TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {[1, 2, 3, 4, 5, 6].map((item) => (
              <TableRow key={item}>
                <TableCell><Skeleton className="h-6 w-6" /></TableCell>
                <TableCell><Skeleton className="h-6 w-32" /></TableCell>
                <TableCell><Skeleton className="h-6 w-40" /></TableCell>
                <TableCell><Skeleton className="h-6 w-20" /></TableCell>
                <TableCell><Skeleton className="h-6 w-24" /></TableCell>
                <TableCell><Skeleton className="h-6 w-24" /></TableCell>
                <TableCell><Skeleton className="h-6 w-8" /></TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    );
  }
  
  if (monitors.length === 0) {
    return (
      <div className="bg-white rounded-md shadow p-8 text-center">
        <p className="text-gray-500">No monitors found. Create a new monitor to get started.</p>
      </div>
    );
  }
  
  return (
    <div className="bg-white rounded-md shadow overflow-hidden">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>S/N</TableHead>
            <TableHead>Monitor Name</TableHead>
            <TableHead>Address</TableHead>
            <TableHead>Status</TableHead>
            <TableHead>Type</TableHead>
            <TableHead>Last Updated</TableHead>
            <TableHead className="w-10"></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {monitors.map((monitor, index) => {
            const isEnabled = monitor.status !== false;
            
            return (
              <TableRow key={monitor.id}>
                <TableCell>{index + 1}</TableCell>
                <TableCell className="font-medium">{monitor.name}</TableCell>
                <TableCell>{monitor.serverAddress}</TableCell>
                <TableCell>
                  <Badge 
                    className={statusVariants[monitor.monitorStatus]}
                  >
                    {monitor.monitorStatus}
                  </Badge>
                </TableCell>
                <TableCell>{monitor.type}</TableCell>
                <TableCell>{formatTimeDifference(monitor.lastNotifiedAt)}</TableCell>
                <TableCell>
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button variant="ghost" size="icon" className="h-8 w-8">
                        <MoreVertical className="h-4 w-4" />
                        <span className="sr-only">Open menu</span>
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent align="end">
                      {isEnabled ? (
                        <DropdownMenuItem 
                          onClick={() => onDisable(monitor.id)}
                          className="flex items-center gap-2"
                        >
                          <X className="h-4 w-4" />
                          <span>Disable</span>
                        </DropdownMenuItem>
                      ) : (
                        <DropdownMenuItem 
                          onClick={() => onEnable(monitor.id)}
                          className="flex items-center gap-2"
                        >
                          <Check className="h-4 w-4" />
                          <span>Enable</span>
                        </DropdownMenuItem>
                      )}
                      <DropdownMenuSeparator />
                      <DropdownMenuItem 
                        onClick={() => onDelete(monitor.id)}
                        className="text-red-600 flex items-center gap-2"
                      >
                        <Trash2 className="h-4 w-4" />
                        <span>Delete</span>
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </div>
  );
};

export default MonitorList;