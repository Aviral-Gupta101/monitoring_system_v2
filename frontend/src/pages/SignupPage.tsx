import React, { useState } from 'react';
import axios from 'axios';
import { toast } from 'sonner';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Button } from "../components/ui/button";
import { Label } from "../components/ui/label";
import { Link } from 'react-router-dom';
import { SERVER_ADDRESS } from '@/lib/global';


const SignupPage: React.FC = () => {
  const [email, setEmail] = useState<string>('');
  const [password, setPassword] = useState<string>('');
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    
    try {
      const response : any = await axios.post(SERVER_ADDRESS + 'auth/signup', { email, password });
      
      if (response.status == 200) {
        toast.success('Signup successful', {
          description: <span className='text-gray-500'>Please Login!</span>,
        });
      }

    } catch (error : any) {
      console.error('Signup error:', error);
      toast.error('Signup failed', {
        description: <span className='text-gray-500'>{(error) 
          ? error.response?.data?.message || "Invalid credentials"
          : "An error occurred"}</span> ,
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <Card className="w-full max-w-md">
        <CardHeader>
          <CardTitle className="text-2xl text-center">Service Monitor</CardTitle>
          <CardDescription className="text-center">Create new account</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="email">Email</Label>
              <Input
                id="email"
                type="email"
                placeholder="your.email@example.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">Password</Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <Button 
              type="submit" 
              className="w-full" 
              disabled={isLoading}
            >
              {isLoading ? "Creating account..." : "Signup"}
            </Button>
          </form>
        </CardContent>
        <CardFooter className="flex justify-center">
          <p className="text-sm text-gray-500">Already have an account?  
            <Link to={"/"} className='hover:underline text-black ml-0.5'>Login</Link>
          </p>
        </CardFooter>
      </Card>
    </div>
  );
};

export default SignupPage;
