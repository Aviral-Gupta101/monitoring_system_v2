import React, { useState } from 'react';
import { 
  Dialog, 
  DialogClose, 
  DialogContent, 
  DialogDescription, 
  DialogFooter, 
  DialogHeader, 
  DialogTitle 
} from "./ui/dialog";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { RadioGroup, RadioGroupItem } from "./ui/radio-group";
import { MonitorFormData, FormErrors } from '../types';

interface CreateMonitorDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onCreate: (monitorData: MonitorFormData) => Promise<void>;
}

interface MonitorType {
  id: 'PORT_CHECK' | 'HTTP_CHECK';
  label: string;
}

const monitorTypes: MonitorType[] = [
  { id: "PORT_CHECK", label: "Port Check" },
  { id: "HTTP_CHECK", label: "HTTP Check" }
];

const isValidIPv4 = (ip: string): boolean => {
  const regex = /^(\d{1,3})\.(\d{1,3})\.(\d{1,3})\.(\d{1,3})$/;
  if (!regex.test(ip)) return false;
  return ip.split('.').every(num => parseInt(num) <= 255);
};

const isValidIPv6 = (ip: string): boolean => {
  const regex = /^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$/;
  return regex.test(ip);
};

const isValidDomain = (domain: string): boolean => {
  const regex = /^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9](?:\.[a-zA-Z]{2,})+$/;
  return regex.test(domain);
};

const CreateMonitorDialog: React.FC<CreateMonitorDialogProps> = ({ open, onOpenChange, onCreate }) => {
  const [formData, setFormData] = useState<MonitorFormData>({
    name: '',
    address: '',
    type: 'PORT_CHECK',
    port: '',
    protocol: 'HTTP', // 👈 Add protocol for HTTP/HTTPS selection
    scheduleInterval: 30, // Initialize scheduleInterval
  });
  
  const [formErrors, setFormErrors] = useState<FormErrors>({});

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    
    if (formErrors[name]) {
      setFormErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleTypeChange = (value: 'PORT_CHECK' | 'HTTP_CHECK') => {
    setFormData(prev => ({
      ...prev,
      type: value,
      port: '', // reset port if switching
      protocol: 'HTTP', // reset protocol if switching
    }));
  };

  const handleProtocolChange = (value: 'HTTP' | 'HTTPS') => {
    setFormData(prev => ({
      ...prev,
      protocol: value,
    }));
  };

  const validateForm = (): boolean => {
    const errors: FormErrors = {};
    
    if (!formData.name.trim()) {
      errors.name = 'Monitor name is required';
    }
    
    if (!formData.address.trim()) {
      errors.address = 'Address is required';
    } else if (
      !isValidIPv4(formData.address) && 
      !isValidIPv6(formData.address) && 
      !isValidDomain(formData.address)
    ) {
      errors.address = 'Invalid IP address or domain';
    }
    
    if (formData.type === 'PORT_CHECK') {
      if (!formData.port || isNaN(parseInt(formData.port)) || parseInt(formData.port) < 1 || parseInt(formData.port) > 65535) {
        errors.port = 'Port must be a number between 1-65535';
      }
    }

    // Validate schedule interval
    if (formData.scheduleInterval && (formData.scheduleInterval < 1 || formData.scheduleInterval > 1440)) {
      errors.scheduleInterval = 'Interval must be between 1 and 1440 minutes (1 day)';
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (validateForm()) {
      onCreate(formData);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <form onSubmit={handleSubmit}>
          <DialogHeader>
            <DialogTitle>Create New Monitor</DialogTitle>
            <DialogDescription>
              Add a new service to monitor its status
            </DialogDescription>
          </DialogHeader>
          
          <div className="grid gap-4 py-4">
            <div className="grid gap-2">
              <Label htmlFor="name">
                Monitor Name <span className="text-red-500">*</span>
              </Label>
              <Input
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                placeholder="My Website Monitor"
              />
              {formErrors.name && (
                <p className="text-sm text-red-500">{formErrors.name}</p>
              )}
            </div>
            
            <div className="grid gap-2">
              <Label htmlFor="address">
                Address (IP or Domain) <span className="text-red-500">*</span>
              </Label>
              <Input
                id="address"
                name="address"
                value={formData.address}
                onChange={handleChange}
                placeholder="192.168.1.1 or example.com"
              />
              {formErrors.address && (
                <p className="text-sm text-red-500">{formErrors.address}</p>
              )}
            </div>

            <div className="grid gap-2">
              <Label htmlFor="scheduleInterval">
                Schedule Interval (in seconds) <span className="text-red-500">*</span>
              </Label>
              <Input
                id="scheduleInterval"
                name="scheduleInterval"
                type="number"
                value={formData.scheduleInterval}
                onChange={handleChange}
                placeholder="Enter schedule interval in minutes"
              />
              {formErrors.scheduleInterval && (
                <p className="text-sm text-red-500">{formErrors.scheduleInterval}</p>
              )}
            </div>
            
            <div className="grid gap-2">
              <Label>
                Monitor Type <span className="text-red-500">*</span>
              </Label>
              <RadioGroup
                value={formData.type}
                onValueChange={handleTypeChange}
                className="flex gap-4"
              >
                {monitorTypes.map(type => (
                  <div key={type.id} className="flex items-center space-x-2">
                    <RadioGroupItem value={type.id} id={type.id} />
                    <Label htmlFor={type.id}>{type.label}</Label>
                  </div>
                ))}
              </RadioGroup>
            </div>

            {formData.type === 'PORT_CHECK' && (
              <div className="grid gap-2">
                <Label htmlFor="port">
                  Port <span className="text-red-500">*</span>
                </Label>
                <Input
                  id="port"
                  name="port"
                  type="number"
                  value={formData.port}
                  onChange={handleChange}
                  placeholder="80"
                />
                {formErrors.port && (
                  <p className="text-sm text-red-500">{formErrors.port}</p>
                )}
              </div>
            )}

            {formData.type === 'HTTP_CHECK' && (
              <div className="grid gap-2">
                <Label>Protocol</Label>
                <RadioGroup
                  value={formData.protocol}
                  onValueChange={handleProtocolChange}
                  className="flex gap-4"
                >
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="HTTP" id="http" />
                    <Label htmlFor="http">HTTP</Label>
                  </div>
                  <div className="flex items-center space-x-2">
                    <RadioGroupItem value="HTTPS" id="https" />
                    <Label htmlFor="https">HTTPS</Label>
                  </div>
                </RadioGroup>
              </div>
            )}

          </div>
          
          <DialogFooter className="flex justify-end gap-2">
            <DialogClose asChild>
              <Button type="button" variant="outline">Cancel</Button>
            </DialogClose>
            <Button type="submit">Create Monitor</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default CreateMonitorDialog;
