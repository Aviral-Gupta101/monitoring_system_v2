import React from 'react';
import { Button } from "./ui/button";

interface HeaderProps {
  onLogout: () => void;
}

const Header: React.FC<HeaderProps> = ({ onLogout }) => {
  return (
    <header className="bg-white shadow">
      <div className="container mx-auto px-4 py-4 flex justify-between items-center">
        <div className="flex items-center gap-2">
          <span className="font-bold text-xl">Service Monitor</span>
        </div>
        <Button variant="outline" onClick={onLogout}>
          Log Out
        </Button>
      </div>
    </header>
  );
};

export default Header;