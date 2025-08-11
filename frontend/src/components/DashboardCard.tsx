import React from 'react';

interface DashboardCardProps {
  title: string;
  count: number;
  icon: string;
  color: string;
}

const DashboardCard: React.FC<DashboardCardProps> = ({
  title,
  count,
  icon,
  color,
}) => {
  return (
    <div className="col-md-3 mb-4">
      <div className="card">
        <div className="card-body">
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h6 className="text-muted mb-2">{title}</h6>
              <h3 className="mb-0">{count}</h3>
            </div>
            <div
              className={`rounded-circle p-3 d-flex align-items-center justify-content-center`}
              style={{ backgroundColor: `${color}20`, width: 60, height: 60 }}
            >
              <i className={`bi ${icon} fs-4`} style={{ color }}></i>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DashboardCard;
