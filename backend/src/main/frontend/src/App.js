import React, { Component } from "react";
import { Routes, Route, Link } from "react-router-dom";
import "./App.css";

import AuthService from "./services/auth.service";

import Login from "./components/login.component";
import BoardPublic from "./components/board-public.component";
import BoardTeacher from "./components/board-teacher.component";
import BoardAdmin from "./components/board-admin.component";
import MapGraph from "./components/map-graph.component";


import AuthVerify from "./common/auth-verify";
import EventBus from "./common/EventBus";

class App extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      showTeacherBoard: false,
      showAdminBoard: false,
      showPublicBoard: true,
      currentUser: undefined,
    };
  }

  componentDidMount() {
    const user = AuthService.getCurrentUser();

    if (user) {
      this.setState({
        currentUser: user,
        showTeacherBoard: user.roles.includes("ROLE_TEACHER"),
        showAdminBoard: user.roles.includes("ROLE_ADMIN"),
        showPublicBoard: false
      });
    }
    
    EventBus.on("logout", () => {
      this.logOut();
    });
  }

  componentWillUnmount() {
    EventBus.remove("logout");
  }

  logOut() {
    AuthService.logout();
    this.setState({
      showTeacherBoard: false,
      showAdminBoard: false,
      showPublicBoard: true,
      currentUser: undefined,
    });
  }

  render() {
    const { currentUser, showPublicBoard, showTeacherBoard, showAdminBoard } = this.state;

    return (
        <div className="container">
          <nav className="navbar">
            <div className="navbar-brand">
              <a href="https://www.pk.edu.pl/"><img src="/img/sygnet_pk.png" className="brand-logo" alt="pk-logo"/></a>
              <a href="https://wisie.pk.edu.pl/"><img src="/img/logo_wisie.png" className="brand-logo" alt="wisie-logo"/></a>
            </div>
            <div className="navbar-links">
              <Link to={"/"} className="nav-item">
                <li>
                  <i className="fa fa-home"></i>
                </li>
              </Link>
              <a href="http://gigw.pl/zaplecze-dydaktyczno-badawcze/zlewnia-badawcza-potoku-sudol/" className="nav-item">
                <li>
                  Aktualności
                </li>
              </a>
              {showPublicBoard && (

                  <Link to={"/public"} className="nav-item">
                    <li>Wykresy</li>
                  </Link>

              )}
              {showTeacherBoard && (
                  <Link to={"/teacher"} className="nav-item">
                    <li>Wykresy</li>
                  </Link>
              )}
              {showAdminBoard && (
                  <Link to={"/admin"} className="nav-item">
                    <li>Wykresy</li>
                  </Link>
              )}
              {currentUser ? (
                  <a href="/" className="nav-item" onClick={this.logOut}>
                    <li>Wyloguj się</li>
                  </a>
              ) : (
                  <Link to={"/login"} className="nav-item">
                    <li>Zaloguj się</li>
                  </Link>

              )}

            </div>
          </nav>
          <div className="title-bar">
            {currentUser ? (
                <h4 className="user-info">Zalogowany jako: {currentUser.username}</h4>
            ): (<h4 className="user-info"></h4>)}
            <h1>ZLEWNIA BADAWCZA POTOKU SUDÓŁ</h1>
          </div>
          <div className="content-container">
            <Routes>
              <Route path="/" element={<MapGraph />} />
              <Route path="/login" element={<Login />} />
              <Route path="/public" element={<BoardPublic />} />
              <Route path="/teacher" element={<BoardTeacher />} />
              <Route path="/admin" element={<BoardAdmin />} />
            </Routes>
          </div>
          <AuthVerify logOut={this.logOut}/>
          <footer className="footer">
            <h3>Katedra Geoinżynierii i Gospodarki Wodnej</h3>
            <div className="footer-contact">
              <ul>
                <li>Wydział Inżynierii Środowiska i Energetyki</li>
                <li>Politechnika Krakowska</li>
                <li><i className="fa fa-flag"></i> ul. Warszawska 24 31-155 Kraków</li>
                <li><i className="fa fa-globe"></i> <a href="http://gigw.pl/">www.gigw.pl</a></li>
              </ul>
              <ul>
                <li><i className="fa fa-phone"></i> +48 12 628 2041</li>
                <li><i className="fa fa-phone"></i> +48 12 628 2810</li>
                <li><i className="fa fa-fax"></i> +48 12 628 3197</li>
                <li><i className="fa fa-envelope-o"></i><a href="mailto:sekretariat-S1@pk.edu.pl">sekretariat-S1@pk.edu.pl</a></li>
              </ul>
            </div>
            <div className="footer-author">
              Strona internetowa została wykonana w ramach pracy inżynierskiej przez Pana Konrada Blicharz, studenta na kierunku Geoinformatyka, w roku akademickim 2023/24 pod kierunkiem dr inż. Elżbiety Jarosińskiej.
            </div>
          </footer>
        </div>
    );
  }
}

export default App;