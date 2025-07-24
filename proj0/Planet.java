public class Planet {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public static final double gConstant = 6.67e-11;

    public Planet(double xP, double yP, double xV, double yV, double m, String img) {
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }

    public Planet(Planet p) {
        xxPos = p.xxPos;
        yyPos = p.yyPos;
        xxVel = p.xxVel;
        yyVel = p.yyVel;
        mass = p.mass;
        imgFileName = p.imgFileName;
    }

    public double calcDistance(Planet p) {
        double distanceSquared = (this.xxPos - p.xxPos) * (this.xxPos - p.xxPos) + (this.yyPos - p.yyPos) * (this.yyPos - p.yyPos);
        double distance = Math.pow(distanceSquared, 0.5);
        return distance;
    }

    public double calcForceExertedBy(Planet p) {
        double distance = this.calcDistance(p);
        double force = this.gConstant * this.mass * p.mass / (distance * distance);
        return force;
    }

    public double calcForceExertedByX(Planet p) {
        double force = this.calcForceExertedBy(p);
        double distance = this.calcDistance(p);
        double dx = p.xxPos - this.xxPos;
        return force * dx / distance;
    }

    public double calcForceExertedByY(Planet p) {
        double force = this.calcForceExertedBy(p);
        double distance = this.calcDistance(p);
        double dy = p.yyPos - this.yyPos;
        return force * dy / distance;
    }

    public double calcNetForceExertedByX(Planet[] planets) {
        double totalForce = 0;
        for (int i = 0; i < planets.length; i++) {
            Planet p = planets[i];
            if (this.equals(p)) {
                continue;
            }
            double force = this.calcForceExertedByX(p);
            totalForce += force;
        }
        return totalForce;
    }

     public double calcNetForceExertedByY(Planet[] planets) {
        double totalForce = 0;
        for (int i = 0; i < planets.length; i++) {
            Planet p = planets[i];
            if (this.equals(p)) {
                continue;
            }
            double force = this.calcForceExertedByY(p);
            totalForce += force;
        }
        return totalForce;
    }

    public void update(double dt, double fX, double fY) {
        double xxAcceleration = fX / this.mass;
        double yyAcceleration = fY / this.mass;
        this.xxVel = this.xxVel + xxAcceleration * dt;
        this.yyVel = this.yyVel + yyAcceleration * dt;
        this.xxPos = this.xxPos + this.xxVel * dt;
        this.yyPos = this.yyPos + this.yyVel * dt;
    }

    public void draw() {
        String filename = "images/" + this.imgFileName;
        StdDraw.picture(this.xxPos, this.yyPos, filename);
        StdDraw.show();
    }
}